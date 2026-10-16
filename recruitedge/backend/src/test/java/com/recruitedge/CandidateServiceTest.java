package com.recruitedge;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.recruitedge.dto.CandidateRequest;
import com.recruitedge.exception.BadRequestException;
import com.recruitedge.model.*;
import com.recruitedge.repository.CandidateRepository;
import com.recruitedge.service.CandidateService;
import com.recruitedge.service.JobService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {
    @Mock CandidateRepository repo;
    @Mock JobService jobService;
    @InjectMocks CandidateService service;

    private Job job(JobStatus status) {
        Job j = new Job();
        j.setId(1L);
        j.setStatus(status);
        return j;
    }

    private final CandidateRequest req = new CandidateRequest("Asha", "asha@mail.com", "999", 1L);

    @Test
    void rejectsApplicationToClosedJob() {
        when(jobService.get(1L)).thenReturn(job(JobStatus.CLOSED));
        assertThrows(BadRequestException.class, () -> service.apply(req));
    }

    @Test
    void rejectsDuplicateApplication() {
        when(jobService.get(1L)).thenReturn(job(JobStatus.OPEN));
        when(repo.existsByEmailAndJobId("asha@mail.com", 1L)).thenReturn(true);
        assertThrows(BadRequestException.class, () -> service.apply(req));
    }

    @Test
    void newCandidateStartsAtApplied() {
        when(jobService.get(1L)).thenReturn(job(JobStatus.OPEN));
        when(repo.existsByEmailAndJobId(any(), any())).thenReturn(false);
        when(repo.save(any(Candidate.class))).thenAnswer(inv -> inv.getArgument(0));
        assertEquals(Stage.APPLIED, service.apply(req).getStage());
    }

    @Test
    void hiredCandidateCannotMoveStage() {
        Candidate c = new Candidate();
        c.setStage(Stage.HIRED);
        when(repo.findById(5L)).thenReturn(Optional.of(c));
        assertThrows(BadRequestException.class, () -> service.moveStage(5L, Stage.OFFER));
    }
}
