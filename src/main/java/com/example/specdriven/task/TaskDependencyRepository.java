package com.example.specdriven.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {

    List<TaskDependency> findByPredecessorIdOrSuccessorId(Long predecessorId, Long successorId);

    void deleteByPredecessorIdOrSuccessorId(Long predecessorId, Long successorId);
}
