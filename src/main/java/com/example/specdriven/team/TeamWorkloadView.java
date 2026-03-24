package com.example.specdriven.team;

import com.example.specdriven.member.Member;
import com.example.specdriven.task.Task;
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

@Route("team")
@PageTitle("Team Workload — Forge")
@PermitAll
public class TeamWorkloadView extends VerticalLayout {

    private final TeamWorkloadService workloadService;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d");

    public TeamWorkloadView(TeamWorkloadService workloadService) {
        this.workloadService = workloadService;
        setPadding(false);
        setSpacing(false);

        Div header = new Div();
        header.addClassName("page-header");
        H2 title = new H2("Team Workload");
        header.add(title);

        Grid<TeamWorkloadService.MemberWorkload> grid = new Grid<>();
        grid.setSizeFull();

        grid.addColumn(w -> w.member().getName())
                .setHeader("Member Name").setSortable(true).setFlexGrow(2);

        grid.addColumn(w -> w.member().getRole().name())
                .setHeader("Role").setSortable(true);

        grid.addColumn(TeamWorkloadService.MemberWorkload::activeTasks)
                .setHeader("Active Tasks").setSortable(true);

        grid.addColumn(TeamWorkloadService.MemberWorkload::completedTasks)
                .setHeader("Completed Tasks").setSortable(true);

        // Row highlighting for overloaded members
        grid.setPartNameGenerator(w -> w.isOverloaded() ? "workload-warning" : null);

        // Item details (expandable)
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createDetailsLayout));

        List<TeamWorkloadService.MemberWorkload> workloads = workloadService.getWorkloads();
        grid.setItems(workloads);

        add(header, grid);
        setFlexGrow(1, grid);
    }

    private Div createDetailsLayout(TeamWorkloadService.MemberWorkload workload) {
        Div container = new Div();
        container.getStyle()
                .set("padding", "var(--vaadin-padding-m)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "var(--vaadin-gap-s)");

        if (workload.tasksByProject().isEmpty()) {
            container.add(new Span("No tasks assigned"));
            return container;
        }

        for (Map.Entry<String, List<Task>> entry : workload.tasksByProject().entrySet()) {
            Span projectHeader = new Span(entry.getKey());
            projectHeader.getStyle()
                    .set("font-weight", "var(--aura-font-weight-semibold)")
                    .set("font-size", "var(--aura-font-size-s)")
                    .set("color", "#212121");
            container.add(projectHeader);

            for (Task task : entry.getValue()) {
                Div taskRow = new Div();
                taskRow.getStyle()
                        .set("display", "flex")
                        .set("align-items", "center")
                        .set("gap", "var(--vaadin-gap-s)")
                        .set("padding-left", "var(--vaadin-padding-m)")
                        .set("cursor", "pointer");

                Span taskName = new Span(task.getName());
                taskName.getStyle().set("font-size", "var(--aura-font-size-s)");

                Span dateRange = new Span(task.getStartDate().format(DATE_FMT)
                        + " — " + task.getEndDate().format(DATE_FMT));
                dateRange.getStyle()
                        .set("color", "#757575")
                        .set("font-size", "var(--aura-font-size-xs)");

                String statusLabel = task.getStatus().name().replace('_', ' ');
                Span statusBadge = new Span(statusLabel);
                statusBadge.addClassName("badge-" + task.getStatus().name().toLowerCase().replace('_', '-'));

                taskRow.add(taskName, dateRange, statusBadge);

                // Navigate to task management on click
                Long projectId = task.getProject().getId();
                taskRow.addClickListener(e ->
                        getUI().ifPresent(ui -> ui.navigate("projects/" + projectId + "/tasks")));

                container.add(taskRow);
            }
        }

        return container;
    }
}
