package com.example.specdriven.data;

import com.example.specdriven.member.Member;
import com.example.specdriven.member.MemberRepository;
import com.example.specdriven.member.MemberRole;
import com.example.specdriven.project.Project;
import com.example.specdriven.project.ProjectRepository;
import com.example.specdriven.task.Task;
import com.example.specdriven.task.TaskDependency;
import com.example.specdriven.task.TaskDependencyRepository;
import com.example.specdriven.task.TaskPriority;
import com.example.specdriven.task.TaskRepository;
import com.example.specdriven.task.TaskStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskDependencyRepository dependencyRepository;

    public DataInitializer(MemberRepository memberRepository,
                           ProjectRepository projectRepository,
                           TaskRepository taskRepository,
                           TaskDependencyRepository dependencyRepository) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.dependencyRepository = dependencyRepository;
    }

    @Override
    public void run(String... args) {
        if (memberRepository.count() > 0) return;

        // Create team members
        Member alice = memberRepository.save(new Member("Alice Martin", "alice@forge.dev", MemberRole.MANAGER));
        Member bob = memberRepository.save(new Member("Bob Chen", "bob@forge.dev", MemberRole.DEVELOPER));
        Member carol = memberRepository.save(new Member("Carol Davis", "carol@forge.dev", MemberRole.DEVELOPER));
        Member dan = memberRepository.save(new Member("Dan Wilson", "dan@forge.dev", MemberRole.DESIGNER));
        Member eve = memberRepository.save(new Member("Eve Brown", "eve@forge.dev", MemberRole.QA));

        // Project 1: Website Redesign (in progress)
        LocalDate now = LocalDate.now();
        Project web = new Project("Website Redesign", "Modernize the company website with new branding",
                now.minusDays(20), now.plusDays(40));
        web.setMembers(Set.of(alice, bob, dan));
        web = projectRepository.save(web);

        Task t1 = createTask("Design mockups", null, now.minusDays(20), now.minusDays(10), web, dan, TaskStatus.DONE, TaskPriority.HIGH);
        Task t2 = createTask("Set up project structure", null, now.minusDays(15), now.minusDays(5), web, bob, TaskStatus.DONE, TaskPriority.HIGH);
        Task t3 = createTask("Implement homepage", null, now.minusDays(5), now.plusDays(10), web, bob, TaskStatus.IN_PROGRESS, TaskPriority.HIGH);
        Task t4 = createTask("Implement about page", null, now.plusDays(5), now.plusDays(20), web, bob, TaskStatus.TODO, TaskPriority.MEDIUM);
        Task t5 = createTask("QA testing", null, now.plusDays(20), now.plusDays(35), web, eve, TaskStatus.TODO, TaskPriority.MEDIUM);

        dependencyRepository.save(new TaskDependency(t1, t3));
        dependencyRepository.save(new TaskDependency(t2, t3));
        dependencyRepository.save(new TaskDependency(t3, t4));
        dependencyRepository.save(new TaskDependency(t4, t5));

        // Project 2: Mobile App (not started)
        Project mobile = new Project("Mobile App", "Native mobile app for iOS and Android",
                now.plusDays(10), now.plusDays(90));
        mobile.setMembers(Set.of(alice, carol, dan, eve));
        mobile = projectRepository.save(mobile);

        createTask("App wireframes", null, now.plusDays(10), now.plusDays(20), mobile, dan, TaskStatus.TODO, TaskPriority.HIGH);
        createTask("API design", null, now.plusDays(10), now.plusDays(25), mobile, carol, TaskStatus.TODO, TaskPriority.HIGH);
        createTask("Backend development", null, now.plusDays(25), now.plusDays(60), mobile, carol, TaskStatus.TODO, TaskPriority.HIGH);
        createTask("Frontend development", null, now.plusDays(30), now.plusDays(70), mobile, carol, TaskStatus.TODO, TaskPriority.MEDIUM);
        createTask("Integration testing", null, now.plusDays(70), now.plusDays(85), mobile, eve, TaskStatus.TODO, TaskPriority.MEDIUM);

        // Project 3: Internal Tools (completed)
        Project tools = new Project("Internal Tools", "Build internal dashboard for operations team",
                now.minusDays(60), now.minusDays(5));
        tools.setMembers(Set.of(bob, carol));
        tools = projectRepository.save(tools);

        createTask("Requirements gathering", null, now.minusDays(60), now.minusDays(50), tools, bob, TaskStatus.DONE, TaskPriority.MEDIUM);
        createTask("Dashboard implementation", null, now.minusDays(50), now.minusDays(20), tools, bob, TaskStatus.DONE, TaskPriority.HIGH);
        createTask("Reporting module", null, now.minusDays(25), now.minusDays(10), tools, carol, TaskStatus.DONE, TaskPriority.MEDIUM);
        createTask("Deploy and documentation", null, now.minusDays(10), now.minusDays(5), tools, carol, TaskStatus.DONE, TaskPriority.LOW);
    }

    private Task createTask(String name, String description, LocalDate start, LocalDate end,
                            Project project, Member assignee, TaskStatus status, TaskPriority priority) {
        Task task = new Task(name, start, end, project);
        task.setDescription(description);
        task.setAssignee(assignee);
        task.setStatus(status);
        task.setPriority(priority);
        return taskRepository.save(task);
    }
}
