package com.example.specdriven.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.assignee WHERE t.project.id = :projectId")
    List<Task> findByProjectIdWithAssignee(Long projectId);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.assignee LEFT JOIN FETCH t.project WHERE t.assignee.id = :memberId")
    List<Task> findByAssigneeIdWithProject(Long memberId);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.assignee LEFT JOIN FETCH t.predecessors p LEFT JOIN FETCH p.predecessor WHERE t.project.id = :projectId")
    List<Task> findByProjectIdWithDependencies(Long projectId);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.assignee LEFT JOIN FETCH t.project WHERE t.id = :id")
    Optional<Task> findByIdWithDetails(Long id);
}
