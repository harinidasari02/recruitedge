package com.recruitedge.service;

import com.recruitedge.dto.CandidateRequest;
import com.recruitedge.exception.BadRequestException;
import com.recruitedge.exception.ResourceNotFoundException;
import com.recruitedge.model.*;
import com.recruitedge.repository.CandidateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository repo;
    private final JobService jobService;

    public List<Candidate> list(Long jobId) {
        return jobId == null ? repo.findAll() : repo.findByJobId(jobId);
    }

    public Candidate get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate not found: " + id));
    }

    public Candidate apply(CandidateRequest r) {
        Job job = jobService.get(r.jobId());
        if (job.getStatus() != JobStatus.OPEN) {
            throw new BadRequestException("This job is closed and no longer accepts candidates.");
        }
        if (repo.existsByEmailAndJobId(r.email(), job.getId())) {
            throw new BadRequestException("This candidate has already applied to this job.");
        }
        Candidate c = new Candidate();
        c.setName(r.name());
        c.setEmail(r.email());
        c.setPhone(r.phone());
        c.setJob(job);
        return repo.save(c);
    }

    public Candidate moveStage(Long id, Stage stage) {
        Candidate c = get(id);
        if (c.getStage() == Stage.HIRED || c.getStage() == Stage.REJECTED) {
            throw new BadRequestException("A " + c.getStage() + " candidate can't be moved to another stage.");
        }
        c.setStage(stage);
        return repo.save(c);
    }
}
