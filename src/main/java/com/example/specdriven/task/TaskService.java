package com.example.specdriven.task;

import com.example.specdriven.project.Project;
import com.example.specdriven.project.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskDependencyRepository dependencyRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       TaskDependencyRepository dependencyRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.dependencyRepository = dependencyRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findByProjectId(Long projectId) {
        return taskRepository.findByProjectIdWithAssignee(projectId);
    }

    @Transactional(readOnly = true)
    public List<Task> findByProjectIdWithDependencies(Long projectId) {
        return taskRepository.findByProjectIdWithDependencies(projectId);
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Task> findByAssigneeId(Long memberId) {
        return taskRepository.findByAssigneeIdWithProject(memberId);
    }

    @Transactional
    public Task save(Task task) {
        validate(task);
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        dependencyRepository.deleteByPredecessorIdOrSuccessorId(id, id);
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task updateDates(Long taskId, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        validate(task);
        return taskRepository.save(task);
    }

    private void validate(Task task) {
        if (task.getName() == null || task.getName().isBlank()) {
            throw new IllegalArgumentException("Task name is required");
        }
        if (task.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (task.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }
        if (task.getEndDate().isBefore(task.getStartDate())) {
            throw new IllegalArgumentException("End date must be on or after start date");
        }
        Project project = task.getProject();
        if (project != null) {
            if (task.getStartDate().isBefore(project.getStartDate())) {
                throw new IllegalArgumentException("Task start date must not be before the project start date");
            }
            if (task.getEndDate().isAfter(project.getEndDate())) {
                throw new IllegalArgumentException("Task end date must not be after the project end date");
            }
        }
    }
}
