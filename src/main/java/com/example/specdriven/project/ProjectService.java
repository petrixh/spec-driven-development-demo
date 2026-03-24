package com.example.specdriven.project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return projectRepository.findAllWithTasksAndMembers();
    }

    @Transactional(readOnly = true)
    public Project findById(Long id) {
        return projectRepository.findByIdWithTasksAndMembers(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    @Transactional
    public Project save(Project project) {
        validate(project);
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    private void validate(Project project) {
        if (project.getName() == null || project.getName().isBlank()) {
            throw new IllegalArgumentException("Project name is required");
        }
        if (project.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (project.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }
        if (!project.getEndDate().isAfter(project.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
