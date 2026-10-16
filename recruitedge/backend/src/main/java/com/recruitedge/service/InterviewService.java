package com.recruitedge.service;

import com.recruitedge.dto.InterviewRequest;
import com.recruitedge.exception.BadRequestException;
import com.recruitedge.exception.ResourceNotFoundException;
import com.recruitedge.model.*;
import com.recruitedge.repository.InterviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository repo;
    private final CandidateService candidateService;

    public List<Interview> findAll() { return repo.findAll(); }

    public Interview schedule(InterviewRequest r) {
        if (!r.scheduledAt().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Interview time must be in the future.");
        }
        if (repo.existsByInterviewerAndScheduledAt(r.interviewer(), r.scheduledAt())) {
            throw new BadRequestException(r.interviewer() + " already has an interview at that time.");
        }
        Candidate c = candidateService.get(r.candidateId());
        if (c.getStage() == Stage.HIRED || c.getStage() == Stage.REJECTED) {
            throw new BadRequestException("Can't schedule an interview for a " + c.getStage() + " candidate.");
        }
        Interview i = new Interview();
        i.setCandidate(c);
        i.setScheduledAt(r.scheduledAt());
        i.setInterviewer(r.interviewer());
        i.setMode(r.mode());
        candidateService.moveStage(c.getId(), Stage.INTERVIEW);
        return repo.save(i);
    }

    public void cancel(Long id) {
        Interview i = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found: " + id));
        repo.delete(i);
    }
}
