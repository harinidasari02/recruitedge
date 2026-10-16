package com.recruitedge.controller;

import com.recruitedge.dto.InterviewRequest;
import com.recruitedge.model.Interview;
import com.recruitedge.service.InterviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService service;

    @GetMapping public List<Interview> all() { return service.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Interview schedule(@Valid @RequestBody InterviewRequest req) { return service.schedule(req); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) { service.cancel(id); }
}
