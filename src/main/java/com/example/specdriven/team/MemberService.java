package com.example.specdriven.team;

import com.example.specdriven.data.Member;
import com.example.specdriven.data.MemberRepository;
import com.example.specdriven.data.Task;
import com.example.specdriven.data.TaskRepository;
import com.example.specdriven.data.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;

    public MemberService(MemberRepository memberRepository, TaskRepository taskRepository) {
        this.memberRepository = memberRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Task> findTasksByMember(Long memberId) {
        return taskRepository.findByAssigneeId(memberId);
    }

    public long countActiveTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.TODO || t.getStatus() == TaskStatus.IN_PROGRESS)
                .count();
    }

    public long countCompletedTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();
    }
}
