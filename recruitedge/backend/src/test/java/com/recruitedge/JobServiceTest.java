package com.recruitedge;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.recruitedge.exception.ResourceNotFoundException;
import com.recruitedge.model.Job;
import com.recruitedge.model.JobStatus;
import com.recruitedge.repository.JobRepository;
import com.recruitedge.service.JobService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {
    @Mock JobRepository repo;
    @InjectMocks JobService service;

    @Test
    void getThrowsWhenJobMissing() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.get(1L));
    }

    @Test
    void createAlwaysStartsOpen() {
        Job job = new Job();
        job.setStatus(JobStatus.CLOSED);
        when(repo.save(any(Job.class))).thenAnswer(inv -> inv.getArgument(0));
        assertEquals(JobStatus.OPEN, service.create(job).getStatus());
    }

    @Test
    void closeSetsStatusClosed() {
        Job job = new Job();
        when(repo.findById(2L)).thenReturn(Optional.of(job));
        when(repo.save(job)).thenReturn(job);
        assertEquals(JobStatus.CLOSED, service.close(2L).getStatus());
    }
}
