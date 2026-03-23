package com.example.specdriven.task;

import com.example.specdriven.data.Member;
import com.example.specdriven.data.MemberRepository;
import com.example.specdriven.data.Task;
import com.example.specdriven.data.TaskDependencyRepository;
import com.example.specdriven.data.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final TaskDependencyRepository taskDependencyRepository;

    public TaskService(TaskRepository taskRepository,
                       MemberRepository memberRepository,
                       TaskDependencyRepository taskDependencyRepository) {
        this.taskRepository = taskRepository;
        this.memberRepository = memberRepository;
        this.taskDependencyRepository = taskDependencyRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        taskDependencyRepository.deleteByPredecessorIdOrSuccessorId(id, id);
        taskRepository.deleteById(id);
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}
