package com.example.specdriven.team;

import com.example.specdriven.data.Member;
import com.example.specdriven.data.Task;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route("team")
@PageTitle("Team Workload — TickTask")
@PermitAll
public class TeamWorkloadView extends VerticalLayout {

    private final MemberService memberService;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d, yyyy");
    private static final int WORKLOAD_THRESHOLD = 5;

    public TeamWorkloadView(MemberService memberService) {
        this.memberService = memberService;
        addClassName("page-content");
        setPadding(false);
        setSpacing(true);

        H2 title = new H2("Team Workload");
        title.getStyle().set("margin", "0");
        add(title);

        Grid<MemberWorkload> grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeight("600px");

        grid.addColumn(mw -> mw.member().getName()).setHeader("Member Name").setSortable(true).setFlexGrow(2);
        grid.addColumn(mw -> mw.member().getRole().name()).setHeader("Role").setSortable(true);
        grid.addColumn(MemberWorkload::activeTasks).setHeader("Active Tasks").setSortable(true);
        grid.addColumn(MemberWorkload::completedTasks).setHeader("Completed Tasks").setSortable(true);

        // Row styling for workload warning
        grid.setPartNameGenerator(mw -> mw.activeTasks() > WORKLOAD_THRESHOLD ? "workload-warning" : null);

        // Expandable detail rows
        grid.setItemDetailsRenderer(new ComponentRenderer<>(mw -> {
            VerticalLayout details = new VerticalLayout();
            details.setPadding(true);
            details.setSpacing(false);

            List<Task> tasks = mw.tasks();
            if (tasks.isEmpty()) {
                details.add(new Span("No tasks assigned"));
                return details;
            }

            // Group by project
            Map<String, List<Task>> byProject = tasks.stream()
                    .collect(Collectors.groupingBy(t -> {
                        if (t.getProject() != null) {
                            return t.getProject().getName() + "|" + t.getProject().getId();
                        }
                        return "Unknown|0";
                    }));

            for (var entry : byProject.entrySet()) {
                String[] parts = entry.getKey().split("\\|");
                String projectName = parts[0];
                String projectId = parts[1];

                Span projectLabel = new Span(projectName);
                projectLabel.getStyle()
                        .set("font-weight", "bold")
                        .set("margin-top", "var(--vaadin-space-s)")
                        .set("display", "block");
                details.add(projectLabel);

                for (Task task : entry.getValue()) {
                    Div taskRow = new Div();
                    taskRow.getStyle()
                            .set("padding", "var(--vaadin-padding-xs) 0 var(--vaadin-padding-xs) var(--vaadin-padding-m)")
                            .set("cursor", "pointer")
                            .set("display", "flex")
                            .set("gap", "var(--vaadin-space-m)")
                            .set("align-items", "center");

                    Span taskName = new Span(task.getName());
                    taskName.getStyle().set("flex-grow", "1");

                    Span dates = new Span(task.getStartDate().format(DATE_FMT) + " — " + task.getEndDate().format(DATE_FMT));
                    dates.getStyle().set("color", "#757575").set("font-size", "var(--aura-font-size-s)");

                    Span status = new Span(task.getStatus().name().replace("_", " "));
                    status.addClassName("badge");
                    switch (task.getStatus()) {
                        case TODO -> status.addClassName("badge-todo");
                        case IN_PROGRESS -> status.addClassName("badge-in-progress");
                        case DONE -> status.addClassName("badge-done");
                    }

                    taskRow.add(taskName, dates, status);
                    taskRow.addClickListener(ev ->
                            getUI().ifPresent(ui -> ui.navigate("projects/" + projectId + "/tasks")));
                    details.add(taskRow);
                }
            }

            return details;
        }));

        // Load data
        List<Member> members = memberService.findAll();
        List<MemberWorkload> workloads = members.stream().map(m -> {
            List<Task> tasks = memberService.findTasksByMember(m.getId());
            long active = memberService.countActiveTasks(tasks);
            long completed = memberService.countCompletedTasks(tasks);
            return new MemberWorkload(m, tasks, active, completed);
        }).toList();

        grid.setItems(workloads);
        add(grid);
    }

    private record MemberWorkload(Member member, List<Task> tasks, long activeTasks, long completedTasks) {}
}
