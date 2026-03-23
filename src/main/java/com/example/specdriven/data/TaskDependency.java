package com.example.specdriven.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_dependencies")
public class TaskDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "predecessor_id")
    private Task predecessor;

    @ManyToOne
    @JoinColumn(name = "successor_id")
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
}
