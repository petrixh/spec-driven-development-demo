package com.example.specdriven.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final TaskDependencyRepository taskDependencyRepository;

    public DataInitializer(ProjectRepository projectRepository,
                           TaskRepository taskRepository,
                           MemberRepository memberRepository,
                           TaskDependencyRepository taskDependencyRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.memberRepository = memberRepository;
        this.taskDependencyRepository = taskDependencyRepository;
    }

    @Override
    public void run(String... args) {
        if (projectRepository.count() > 0) {
            return;
        }

        // Create members
        Member alice = memberRepository.save(new Member("Alice Johnson", "alice@example.com", MemberRole.MANAGER));
        Member bob = memberRepository.save(new Member("Bob Smith", "bob@example.com", MemberRole.DEVELOPER));
        Member carol = memberRepository.save(new Member("Carol Davis", "carol@example.com", MemberRole.DESIGNER));
        Member dave = memberRepository.save(new Member("Dave Wilson", "dave@example.com", MemberRole.QA));
        Member eve = memberRepository.save(new Member("Eve Martinez", "eve@example.com", MemberRole.DEVELOPER));

        // Project 1: Website Redesign (mixed statuses -> IN_PROGRESS)
        Project webRedesign = new Project("Website Redesign",
                "Complete overhaul of the company website with modern design",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        webRedesign.setMembers(Set.of(alice, bob, carol, dave));
        webRedesign = projectRepository.save(webRedesign);

        Task wr1 = taskRepository.save(new Task("Design mockups", "Create UI mockups for all pages",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 15),
                TaskStatus.DONE, TaskPriority.HIGH, webRedesign, carol));
        Task wr2 = taskRepository.save(new Task("Implement homepage", "Build the new homepage",
                LocalDate.of(2026, 3, 16), LocalDate.of(2026, 4, 15),
                TaskStatus.DONE, TaskPriority.HIGH, webRedesign, bob));
        Task wr3 = taskRepository.save(new Task("Implement about page", "Build the about page",
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30),
                TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, webRedesign, bob));
        Task wr4 = taskRepository.save(new Task("Contact form integration", "Set up contact form with email",
                LocalDate.of(2026, 4, 15), LocalDate.of(2026, 5, 15),
                TaskStatus.TODO, TaskPriority.MEDIUM, webRedesign, eve));
        Task wr5 = taskRepository.save(new Task("QA testing", "Full regression testing",
                LocalDate.of(2026, 5, 16), LocalDate.of(2026, 6, 15),
                TaskStatus.TODO, TaskPriority.HIGH, webRedesign, dave));
        Task wr6 = taskRepository.save(new Task("Launch preparation", "DNS, SSL, deployment scripts",
                LocalDate.of(2026, 6, 16), LocalDate.of(2026, 6, 30),
                TaskStatus.TODO, TaskPriority.LOW, webRedesign, alice));

        // Dependencies for web redesign
        taskDependencyRepository.save(new TaskDependency(wr1, wr2));
        taskDependencyRepository.save(new TaskDependency(wr1, wr3));
        taskDependencyRepository.save(new TaskDependency(wr2, wr4));
        taskDependencyRepository.save(new TaskDependency(wr4, wr5));
        taskDependencyRepository.save(new TaskDependency(wr5, wr6));

        // Project 2: Mobile App (all TODO -> NOT_STARTED)
        Project mobileApp = new Project("Mobile App",
                "Cross-platform mobile app for customer self-service",
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 9, 30));
        mobileApp.setMembers(Set.of(alice, bob, eve));
        mobileApp = projectRepository.save(mobileApp);

        taskRepository.save(new Task("Requirements gathering", "Define app requirements",
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 15),
                TaskStatus.TODO, TaskPriority.HIGH, mobileApp, alice));
        taskRepository.save(new Task("UI/UX design", "Design mobile screens",
                LocalDate.of(2026, 4, 16), LocalDate.of(2026, 5, 15),
                TaskStatus.TODO, TaskPriority.HIGH, mobileApp, carol));
        taskRepository.save(new Task("Backend API", "Build REST API endpoints",
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 7, 31),
                TaskStatus.TODO, TaskPriority.MEDIUM, mobileApp, bob));
        taskRepository.save(new Task("Mobile frontend", "Implement mobile UI",
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 8, 31),
                TaskStatus.TODO, TaskPriority.MEDIUM, mobileApp, eve));

        // Project 3: API Integration (all DONE -> COMPLETED)
        Project apiIntegration = new Project("API Integration",
                "Integrate third-party payment and analytics APIs",
                LocalDate.of(2026, 1, 15), LocalDate.of(2026, 3, 15));
        apiIntegration.setMembers(Set.of(bob, dave));
        apiIntegration = projectRepository.save(apiIntegration);

        taskRepository.save(new Task("Payment API setup", "Integrate Stripe payment API",
                LocalDate.of(2026, 1, 15), LocalDate.of(2026, 2, 15),
                TaskStatus.DONE, TaskPriority.HIGH, apiIntegration, bob));
        taskRepository.save(new Task("Analytics integration", "Set up analytics tracking",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28),
                TaskStatus.DONE, TaskPriority.MEDIUM, apiIntegration, eve));
        taskRepository.save(new Task("Integration testing", "End-to-end integration tests",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 15),
                TaskStatus.DONE, TaskPriority.HIGH, apiIntegration, dave));
    }
}
