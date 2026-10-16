package com.recruitedge.controller;

import com.recruitedge.dto.CandidateRequest;
import com.recruitedge.dto.StageRequest;
import com.recruitedge.model.Candidate;
import com.recruitedge.service.CandidateService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService service;

    @GetMapping
    public List<Candidate> list(@RequestParam(required = false) Long jobId) { return service.list(jobId); }

    @GetMapping("/{id}") public Candidate one(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidate apply(@Valid @RequestBody CandidateRequest req) { return service.apply(req); }

    @PatchMapping("/{id}/stage")
    public Candidate stage(@PathVariable Long id, @Valid @RequestBody StageRequest req) {
        return service.moveStage(id, req.stage());
    }
}
