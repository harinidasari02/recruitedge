package com.recruitedge;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.recruitedge.dto.InterviewRequest;
import com.recruitedge.exception.BadRequestException;
import com.recruitedge.model.*;
import com.recruitedge.repository.InterviewRepository;
import com.recruitedge.service.CandidateService;
import com.recruitedge.service.InterviewService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {
    @Mock InterviewRepository repo;
    @Mock CandidateService candidateService;
    @InjectMocks InterviewService service;

    @Test
    void rejectsPastInterviewTime() {
        var req = new InterviewRequest(1L, LocalDateTime.now().minusDays(1), "Ravi", "Online");
        assertThrows(BadRequestException.class, () -> service.schedule(req));
    }

    @Test
    void rejectsInterviewerDoubleBooking() {
        LocalDateTime when = LocalDateTime.now().plusDays(2);
        when(repo.existsByInterviewerAndScheduledAt("Ravi", when)).thenReturn(true);
        assertThrows(BadRequestException.class,
                () -> service.schedule(new InterviewRequest(1L, when, "Ravi", "Online")));
    }

    @Test
    void schedulingMovesCandidateToInterviewStage() {
        LocalDateTime when = LocalDateTime.now().plusDays(2);
        Candidate c = new Candidate();
        c.setId(1L);
        c.setStage(Stage.SCREENING);
        when(repo.existsByInterviewerAndScheduledAt("Ravi", when)).thenReturn(false);
        when(candidateService.get(1L)).thenReturn(c);
        when(repo.save(any(Interview.class))).thenAnswer(inv -> inv.getArgument(0));

        Interview i = service.schedule(new InterviewRequest(1L, when, "Ravi", "Online"));

        assertEquals("Ravi", i.getInterviewer());
        verify(candidateService).moveStage(1L, Stage.INTERVIEW);
    }
}
