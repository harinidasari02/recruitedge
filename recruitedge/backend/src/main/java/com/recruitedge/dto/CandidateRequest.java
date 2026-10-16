package com.recruitedge.dto;

import jakarta.validation.constraints.*;

public record CandidateRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required") @Email(message = "Email is not valid") String email,
        String phone,
        @NotNull(message = "Job is required") Long jobId) {}
