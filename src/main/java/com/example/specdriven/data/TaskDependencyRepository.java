package com.example.specdriven.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {
    List<TaskDependency> findByPredecessorId(Long taskId);
    List<TaskDependency> findBySuccessorId(Long taskId);
    List<TaskDependency> findByPredecessorProjectId(Long projectId);
    void deleteByPredecessorIdOrSuccessorId(Long predecessorId, Long successorId);
}
