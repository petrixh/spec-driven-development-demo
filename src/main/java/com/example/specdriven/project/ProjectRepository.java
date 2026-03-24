package com.example.specdriven.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.tasks LEFT JOIN FETCH p.members")
    List<Project> findAllWithTasksAndMembers();

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tasks LEFT JOIN FETCH p.members WHERE p.id = :id")
    Optional<Project> findByIdWithTasksAndMembers(Long id);
}
