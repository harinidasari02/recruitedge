package com.recruitedge.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record InterviewRequest(
        @NotNull(message = "Candidate is required") Long candidateId,
        @NotNull(message = "Interview time is required") LocalDateTime scheduledAt,
        @NotBlank(message = "Interviewer is required") String interviewer,
        String mode) {}
