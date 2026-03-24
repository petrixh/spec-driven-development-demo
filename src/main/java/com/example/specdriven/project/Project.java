package com.example.specdriven.project;

import com.example.specdriven.member.Member;
import com.example.specdriven.task.Task;
import com.example.specdriven.task.TaskStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "project_members",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> members = new HashSet<>();

    public Project() {}

    public Project(String name, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public Set<Member> getMembers() { return members; }
    public void setMembers(Set<Member> members) { this.members = members; }

    @Transient
    public ProjectStatus getStatus() {
        if (tasks.isEmpty()) return ProjectStatus.NOT_STARTED;
        boolean allDone = tasks.stream().allMatch(t -> t.getStatus() == TaskStatus.DONE);
        if (allDone) return ProjectStatus.COMPLETED;
        boolean allTodo = tasks.stream().allMatch(t -> t.getStatus() == TaskStatus.TODO);
        if (allTodo) return ProjectStatus.NOT_STARTED;
        return ProjectStatus.IN_PROGRESS;
    }

    @Transient
    public int getProgressPercent() {
        if (tasks.isEmpty()) return 0;
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        return (int) (done * 100 / tasks.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project p)) return false;
        return id != null && Objects.equals(id, p.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
