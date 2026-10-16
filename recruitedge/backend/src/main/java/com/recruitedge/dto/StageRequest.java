package com.recruitedge.dto;

import com.recruitedge.model.Stage;
import jakarta.validation.constraints.NotNull;

public record StageRequest(@NotNull(message = "Stage is required") Stage stage) {}
