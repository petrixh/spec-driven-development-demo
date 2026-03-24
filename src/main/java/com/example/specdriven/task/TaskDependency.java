package com.example.specdriven.task;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "task_dependencies")
public class TaskDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predecessor_id", nullable = false)
    private Task predecessor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "successor_id", nullable = false)
    private Task successor;

    public TaskDependency() {}

    public TaskDependency(Task predecessor, Task successor) {
        this.predecessor = predecessor;
        this.successor = successor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Task getPredecessor() { return predecessor; }
    public void setPredecessor(Task predecessor) { this.predecessor = predecessor; }

    public Task getSuccessor() { return successor; }
    public void setSuccessor(Task successor) { this.successor = successor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDependency td)) return false;
        return id != null && Objects.equals(id, td.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
