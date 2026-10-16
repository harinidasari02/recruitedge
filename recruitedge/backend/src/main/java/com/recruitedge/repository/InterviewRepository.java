package com.recruitedge.repository;

import com.recruitedge.model.Interview;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    boolean existsByInterviewerAndScheduledAt(String interviewer, LocalDateTime scheduledAt);
}
