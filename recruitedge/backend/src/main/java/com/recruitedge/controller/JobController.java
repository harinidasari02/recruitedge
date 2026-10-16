package com.recruitedge.controller;

import com.recruitedge.model.Job;
import com.recruitedge.service.JobService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService service;

    @GetMapping public List<Job> all() { return service.findAll(); }
    @GetMapping("/{id}") public Job one(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Job create(@Valid @RequestBody Job job) { return service.create(job); }

    @PutMapping("/{id}")
    public Job update(@PathVariable Long id, @Valid @RequestBody Job job) { return service.update(id, job); }

    @PatchMapping("/{id}/close")
    public Job close(@PathVariable Long id) { return service.close(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
