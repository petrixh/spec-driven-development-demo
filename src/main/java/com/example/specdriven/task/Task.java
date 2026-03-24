package com.example.specdriven.task;

import com.example.specdriven.member.Member;
import com.example.specdriven.project.Project;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private Member assignee;

    @OneToMany(mappedBy = "successor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskDependency> predecessors = new ArrayList<>();

    @OneToMany(mappedBy = "predecessor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskDependency> successors = new ArrayList<>();

    public Task() {}

    public Task(String name, LocalDate startDate, LocalDate endDate, Project project) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public Member getAssignee() { return assignee; }
    public void setAssignee(Member assignee) { this.assignee = assignee; }

    public List<TaskDependency> getPredecessors() { return predecessors; }
    public void setPredecessors(List<TaskDependency> predecessors) { this.predecessors = predecessors; }

    public List<TaskDependency> getSuccessors() { return successors; }
    public void setSuccessors(List<TaskDependency> successors) { this.successors = successors; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task t)) return false;
        return id != null && Objects.equals(id, t.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
