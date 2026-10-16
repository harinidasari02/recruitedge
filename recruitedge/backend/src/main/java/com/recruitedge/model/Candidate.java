package com.recruitedge.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "job_id"}))
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Stage stage = Stage.APPLIED;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;
}
