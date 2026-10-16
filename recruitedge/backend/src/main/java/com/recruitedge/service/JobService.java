package com.recruitedge.service;

import com.recruitedge.exception.ResourceNotFoundException;
import com.recruitedge.model.Job;
import com.recruitedge.model.JobStatus;
import com.recruitedge.repository.JobRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository repo;

    public List<Job> findAll() { return repo.findAll(); }

    public Job get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job not found: " + id));
    }

    public Job create(Job job) {
        job.setId(null);
        job.setStatus(JobStatus.OPEN);
        return repo.save(job);
    }

    public Job update(Long id, Job in) {
        Job job = get(id);
        job.setTitle(in.getTitle());
        job.setDepartment(in.getDepartment());
        job.setLocation(in.getLocation());
        job.setDescription(in.getDescription());
        return repo.save(job);
    }

    public Job close(Long id) {
        Job job = get(id);
        job.setStatus(JobStatus.CLOSED);
        return repo.save(job);
    }

    public void delete(Long id) { repo.delete(get(id)); }
}
