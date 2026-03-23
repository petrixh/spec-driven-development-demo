package com.example.specdriven.project;

import com.example.specdriven.data.Project;
import com.example.specdriven.data.ProjectRepository;
import com.example.specdriven.data.ProjectStatus;
import com.example.specdriven.data.Task;
import com.example.specdriven.data.TaskStatus;
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
        return projectRepository.findAllWithTasks();
    }

    @Transactional(readOnly = true)
    public Project findById(Long id) {
        return projectRepository.findByIdWithTasks(id).orElse(null);
    }

    @Transactional
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public ProjectStatus calculateStatus(Project project) {
        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            return ProjectStatus.NOT_STARTED;
        }
        boolean allDone = tasks.stream().allMatch(t -> t.getStatus() == TaskStatus.DONE);
        if (allDone) {
            return ProjectStatus.COMPLETED;
        }
        boolean allTodo = tasks.stream().allMatch(t -> t.getStatus() == TaskStatus.TODO);
        if (allTodo) {
            return ProjectStatus.NOT_STARTED;
        }
        return ProjectStatus.IN_PROGRESS;
    }

    public int calculateProgressPercent(Project project) {
        List<Task> tasks = project.getTasks();
        if (tasks.isEmpty()) {
            return 0;
        }
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        return (int) (done * 100 / tasks.size());
    }
}
