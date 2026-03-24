package com.example.specdriven.team;

import com.example.specdriven.member.Member;
import com.example.specdriven.member.MemberRepository;
import com.example.specdriven.task.Task;
import com.example.specdriven.task.TaskRepository;
import com.example.specdriven.task.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamWorkloadService {

    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;

    public TeamWorkloadService(MemberRepository memberRepository, TaskRepository taskRepository) {
        this.memberRepository = memberRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberWorkload> getWorkloads() {
        List<Member> members = memberRepository.findAll();
        List<MemberWorkload> workloads = new ArrayList<>();

        for (Member member : members) {
            List<Task> tasks = taskRepository.findByAssigneeIdWithProject(member.getId());

            long active = tasks.stream()
                    .filter(t -> t.getStatus() == TaskStatus.TODO || t.getStatus() == TaskStatus.IN_PROGRESS)
                    .count();
            long completed = tasks.stream()
                    .filter(t -> t.getStatus() == TaskStatus.DONE)
                    .count();

            // Group tasks by project
            Map<String, List<Task>> tasksByProject = new LinkedHashMap<>();
            for (Task task : tasks) {
                String projectName = task.getProject().getName();
                tasksByProject.computeIfAbsent(projectName, k -> new ArrayList<>()).add(task);
            }

            workloads.add(new MemberWorkload(member, (int) active, (int) completed, tasks, tasksByProject));
        }

        return workloads;
    }

    public record MemberWorkload(
            Member member,
            int activeTasks,
            int completedTasks,
            List<Task> allTasks,
            Map<String, List<Task>> tasksByProject
    ) {
        public boolean isOverloaded() {
            return activeTasks > 5;
        }
    }
}
