package com.recruitedge.repository;

import com.recruitedge.model.Candidate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByJobId(Long jobId);
    boolean existsByEmailAndJobId(String email, Long jobId);
}
