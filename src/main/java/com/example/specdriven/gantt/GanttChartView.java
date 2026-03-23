package com.example.specdriven.gantt;

import com.example.specdriven.data.Project;
import com.example.specdriven.data.Task;
import com.example.specdriven.data.TaskDependency;
import com.example.specdriven.data.TaskDependencyRepository;
import com.example.specdriven.data.TaskStatus;
import com.example.specdriven.project.ProjectService;
import com.example.specdriven.task.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("projects/:projectId/gantt")
@PageTitle("Gantt Chart — TickTask")
@PermitAll
public class GanttChartView extends VerticalLayout implements BeforeEnterObserver {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final TaskDependencyRepository dependencyRepository;
    private final AuthenticationContext authContext;

    private Project project;
    private List<Task> tasks;
    private List<TaskDependency> dependencies;
    private String zoomLevel = "Week";

    private static final int ROW_HEIGHT = 40;
    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.ofPattern("d");
    private static final DateTimeFormatter WEEK_FMT = DateTimeFormatter.ofPattern("MMM d");
    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MMM yyyy");

    public GanttChartView(TaskService taskService, ProjectService projectService,
                          TaskDependencyRepository dependencyRepository,
                          AuthenticationContext authContext) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.dependencyRepository = dependencyRepository;
        this.authContext = authContext;
        addClassName("page-content");
        setPadding(false);
        setSpacing(true);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String projectIdStr = event.getRouteParameters().get("projectId").orElse(null);
        if (projectIdStr == null) {
            event.forwardTo("projects");
            return;
        }
        project = projectService.findById(Long.parseLong(projectIdStr));
        if (project == null) {
            event.forwardTo("projects");
            return;
        }
        tasks = taskService.findByProjectId(project.getId());
        dependencies = dependencyRepository.findByPredecessorProjectId(project.getId());
        buildUI();
    }

    private void buildUI() {
        removeAll();

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H2 title = new H2(project.getName() + " — Gantt Chart");
        title.getStyle().set("margin", "0");

        HorizontalLayout controls = new HorizontalLayout();
        controls.setAlignItems(FlexComponent.Alignment.CENTER);

        Button dayBtn = new Button("Day", e -> { zoomLevel = "Day"; buildUI(); });
        Button weekBtn = new Button("Week", e -> { zoomLevel = "Week"; buildUI(); });
        Button monthBtn = new Button("Month", e -> { zoomLevel = "Month"; buildUI(); });

        if ("Day".equals(zoomLevel)) dayBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        if ("Week".equals(zoomLevel)) weekBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        if ("Month".equals(zoomLevel)) monthBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button backBtn = new Button("Back to Tasks", e ->
                getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/tasks")));
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        controls.add(dayBtn, weekBtn, monthBtn, backBtn);
        header.add(title, controls);
        add(header);

        // Split layout: task list + timeline
        SplitLayout split = new SplitLayout();
        split.setSizeFull();
        split.setSplitterPosition(20);

        // Left: task names
        VerticalLayout taskList = new VerticalLayout();
        taskList.setPadding(false);
        taskList.setSpacing(false);
        taskList.setWidth("100%");

        // Header for task names
        Div taskListHeader = new Div();
        taskListHeader.getStyle()
                .set("height", "32px")
                .set("line-height", "32px")
                .set("font-weight", "bold")
                .set("padding-left", "var(--vaadin-padding-s)")
                .set("border-bottom", "1px solid #E0E0E0")
                .set("background", "#FAFAFA")
                .set("font-size", "var(--aura-font-size-s)")
                .set("color", "#757575");
        taskListHeader.setText("Task");
        taskList.add(taskListHeader);

        for (Task task : tasks) {
            Div row = new Div();
            row.getStyle()
                    .set("height", ROW_HEIGHT + "px")
                    .set("line-height", ROW_HEIGHT + "px")
                    .set("padding-left", "var(--vaadin-padding-s)")
                    .set("border-bottom", "1px solid #F5F5F5")
                    .set("overflow", "hidden")
                    .set("text-overflow", "ellipsis")
                    .set("white-space", "nowrap")
                    .set("font-size", "var(--aura-font-size-s)")
                    .set("cursor", "pointer");
            row.setText(task.getName());
            row.addClickListener(e ->
                    getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/tasks")));
            taskList.add(row);
        }

        // Right: timeline
        Div timelineContainer = new Div();
        timelineContainer.getStyle()
                .set("position", "relative")
                .set("overflow-x", "auto")
                .set("overflow-y", "hidden")
                .set("width", "100%")
                .set("height", "100%");

        LocalDate timelineStart = project.getStartDate();
        LocalDate timelineEnd = project.getEndDate();
        long totalDays = ChronoUnit.DAYS.between(timelineStart, timelineEnd) + 1;

        int cellWidth = switch (zoomLevel) {
            case "Day" -> 30;
            case "Week" -> 16;
            case "Month" -> 4;
            default -> 16;
        };
        int totalWidth = (int) (totalDays * cellWidth);

        Div timeline = new Div();
        timeline.getStyle()
                .set("position", "relative")
                .set("width", totalWidth + "px")
                .set("min-height", (32 + tasks.size() * ROW_HEIGHT + 20) + "px");

        // Timeline header
        Div headerRow = new Div();
        headerRow.addClassName("gantt-timeline-header");
        headerRow.getStyle().set("width", totalWidth + "px");

        if ("Day".equals(zoomLevel)) {
            for (long d = 0; d < totalDays; d++) {
                LocalDate date = timelineStart.plusDays(d);
                Div cell = new Div();
                cell.addClassName("gantt-timeline-cell");
                cell.getStyle().set("width", cellWidth + "px");
                cell.setText(date.format(DAY_FMT));
                headerRow.add(cell);
            }
        } else if ("Week".equals(zoomLevel)) {
            LocalDate current = timelineStart;
            while (!current.isAfter(timelineEnd)) {
                LocalDate weekEnd = current.plusDays(6);
                if (weekEnd.isAfter(timelineEnd)) weekEnd = timelineEnd;
                long days = ChronoUnit.DAYS.between(current, weekEnd) + 1;
                Div cell = new Div();
                cell.addClassName("gantt-timeline-cell");
                cell.getStyle().set("width", (days * cellWidth) + "px");
                cell.setText(current.format(WEEK_FMT));
                headerRow.add(cell);
                current = weekEnd.plusDays(1);
            }
        } else { // Month
            LocalDate current = timelineStart;
            while (!current.isAfter(timelineEnd)) {
                LocalDate monthEnd = current.withDayOfMonth(current.lengthOfMonth());
                if (monthEnd.isAfter(timelineEnd)) monthEnd = timelineEnd;
                long days = ChronoUnit.DAYS.between(current, monthEnd) + 1;
                Div cell = new Div();
                cell.addClassName("gantt-timeline-cell");
                cell.getStyle().set("width", (days * cellWidth) + "px");
                cell.setText(current.format(MONTH_FMT));
                headerRow.add(cell);
                current = monthEnd.plusDays(1);
            }
        }
        timeline.add(headerRow);

        // Task bars
        Map<Long, Integer> taskRowIndex = new HashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            taskRowIndex.put(task.getId(), i);

            long startOffset = ChronoUnit.DAYS.between(timelineStart, task.getStartDate());
            long duration = ChronoUnit.DAYS.between(task.getStartDate(), task.getEndDate()) + 1;

            if (startOffset < 0) startOffset = 0;
            if (startOffset + duration > totalDays) duration = totalDays - startOffset;

            int left = (int) (startOffset * cellWidth);
            int width = (int) (duration * cellWidth);
            int top = 32 + i * ROW_HEIGHT + 6; // 32px header + row offset + centering

            Div bar = new Div();
            bar.addClassName("gantt-bar");
            switch (task.getStatus()) {
                case TODO -> bar.addClassName("gantt-bar-todo");
                case IN_PROGRESS -> bar.addClassName("gantt-bar-in-progress");
                case DONE -> bar.addClassName("gantt-bar-done");
            }
            bar.getStyle()
                    .set("left", left + "px")
                    .set("top", top + "px")
                    .set("width", width + "px");
            bar.setText(task.getName());

            // Click to navigate to task editing
            bar.addClickListener(e ->
                    getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/tasks")));

            // Admin drag support via JS interop
            if (authContext.hasRole("ADMIN")) {
                bar.getStyle().set("cursor", "move");
                bar.getElement().setAttribute("data-task-id", String.valueOf(task.getId()));
                bar.getElement().setAttribute("data-cell-width", String.valueOf(cellWidth));
                bar.getElement().setAttribute("data-min-left", "0");
                bar.getElement().setAttribute("data-max-right", String.valueOf(totalWidth));
                bar.getElement().setAttribute("data-original-left", String.valueOf(left));
                bar.getElement().setAttribute("data-original-width", String.valueOf(width));
                enableDragResize(bar, task, cellWidth, timelineStart);
            }

            timeline.add(bar);

            // Row background line
            Div rowLine = new Div();
            rowLine.addClassName("gantt-row");
            rowLine.getStyle()
                    .set("position", "absolute")
                    .set("left", "0")
                    .set("width", totalWidth + "px")
                    .set("top", (32 + i * ROW_HEIGHT) + "px");
            timeline.add(rowLine);
        }

        // Today marker
        LocalDate today = LocalDate.now();
        if (!today.isBefore(timelineStart) && !today.isAfter(timelineEnd)) {
            long todayOffset = ChronoUnit.DAYS.between(timelineStart, today);
            Div todayMarker = new Div();
            todayMarker.addClassName("gantt-today-marker");
            todayMarker.getStyle()
                    .set("left", (todayOffset * cellWidth) + "px")
                    .set("top", "32px")
                    .set("height", (tasks.size() * ROW_HEIGHT) + "px");
            timeline.add(todayMarker);
        }

        // Dependency arrows (SVG)
        if (!dependencies.isEmpty()) {
            StringBuilder svg = new StringBuilder();
            svg.append("<svg style='position:absolute;top:0;left:0;width:").append(totalWidth)
                    .append("px;height:").append(32 + tasks.size() * ROW_HEIGHT + 20)
                    .append("px;pointer-events:none;z-index:5;'>");
            svg.append("<defs><marker id='arrowhead' markerWidth='8' markerHeight='6' refX='8' refY='3' orient='auto'>");
            svg.append("<polygon points='0 0, 8 3, 0 6' fill='#757575'/></marker></defs>");

            for (TaskDependency dep : dependencies) {
                Integer predIdx = taskRowIndex.get(dep.getPredecessor().getId());
                Integer succIdx = taskRowIndex.get(dep.getSuccessor().getId());
                if (predIdx == null || succIdx == null) continue;

                Task pred = tasks.get(predIdx);
                Task succ = tasks.get(succIdx);

                long predEnd = ChronoUnit.DAYS.between(timelineStart, pred.getEndDate()) + 1;
                long succStart = ChronoUnit.DAYS.between(timelineStart, succ.getStartDate());

                int x1 = (int) (predEnd * cellWidth);
                int y1 = 32 + predIdx * ROW_HEIGHT + ROW_HEIGHT / 2;
                int x2 = (int) (succStart * cellWidth);
                int y2 = 32 + succIdx * ROW_HEIGHT + ROW_HEIGHT / 2;

                svg.append("<path d='M").append(x1).append(",").append(y1);
                svg.append(" C").append(x1 + 20).append(",").append(y1);
                svg.append(" ").append(x2 - 20).append(",").append(y2);
                svg.append(" ").append(x2).append(",").append(y2);
                svg.append("' stroke='#757575' stroke-width='1.5' fill='none' marker-end='url(#arrowhead)'/>");
            }

            svg.append("</svg>");

            Div svgContainer = new Div();
            svgContainer.getElement().setProperty("innerHTML", svg.toString());
            svgContainer.getStyle()
                    .set("position", "absolute")
                    .set("top", "0")
                    .set("left", "0")
                    .set("pointer-events", "none");
            timeline.add(svgContainer);
        }

        timelineContainer.add(timeline);
        split.addToPrimary(taskList);
        split.addToSecondary(timelineContainer);
        add(split);
    }

    private void enableDragResize(Div bar, Task task, int cellWidth, LocalDate timelineStart) {
        long taskId = task.getId();
        // Use $server callback to handle drag results
        getElement().executeJs(
                "const bar = $0;" +
                "const view = $1;" +
                "const cw = $2;" +
                "let startX, origLeft, origWidth, mode;" +
                "bar.addEventListener('mousedown', function(e) {" +
                "  const rect = bar.getBoundingClientRect();" +
                "  mode = (e.clientX > rect.right - 8) ? 'resize' : 'move';" +
                "  startX = e.clientX;" +
                "  origLeft = parseInt(bar.style.left);" +
                "  origWidth = parseInt(bar.style.width);" +
                "  e.preventDefault();" +
                "  e.stopPropagation();" +
                "  function onMove(ev) {" +
                "    const dx = ev.clientX - startX;" +
                "    if (mode === 'move') { bar.style.left = Math.max(0, origLeft + dx) + 'px'; }" +
                "    else { bar.style.width = Math.max(cw, origWidth + dx) + 'px'; }" +
                "  }" +
                "  function onUp() {" +
                "    document.removeEventListener('mousemove', onMove);" +
                "    document.removeEventListener('mouseup', onUp);" +
                "    const daysDelta = Math.round((parseInt(bar.style.left) - origLeft) / cw);" +
                "    const durDelta = Math.round((parseInt(bar.style.width) - origWidth) / cw);" +
                "    if (daysDelta !== 0 || durDelta !== 0) {" +
                "      view.$server.onGanttUpdate(" + taskId + ", daysDelta, durDelta, mode);" +
                "    }" +
                "  }" +
                "  document.addEventListener('mousemove', onMove);" +
                "  document.addEventListener('mouseup', onUp);" +
                "});",
                bar.getElement(), getElement(), cellWidth
        );
    }

    @com.vaadin.flow.component.ClientCallable
    public void onGanttUpdate(long taskId, int daysDelta, int durationDelta, String mode) {
        Task task = taskService.findById(taskId);
        if (task == null) return;

        if ("move".equals(mode)) {
            LocalDate newStart = task.getStartDate().plusDays(daysDelta);
            LocalDate newEnd = task.getEndDate().plusDays(daysDelta);
            if (newStart.isBefore(project.getStartDate())) {
                long diff = ChronoUnit.DAYS.between(newStart, project.getStartDate());
                newStart = project.getStartDate();
                newEnd = newEnd.plusDays(diff);
            }
            if (newEnd.isAfter(project.getEndDate())) {
                long diff = ChronoUnit.DAYS.between(project.getEndDate(), newEnd);
                newEnd = project.getEndDate();
                newStart = newStart.minusDays(diff);
            }
            task.setStartDate(newStart);
            task.setEndDate(newEnd);
        } else {
            LocalDate newEnd = task.getEndDate().plusDays(durationDelta);
            if (newEnd.isAfter(project.getEndDate())) newEnd = project.getEndDate();
            if (newEnd.isBefore(task.getStartDate())) newEnd = task.getStartDate();
            task.setEndDate(newEnd);
        }

        taskService.save(task);
        tasks = taskService.findByProjectId(project.getId());
        dependencies = dependencyRepository.findByPredecessorProjectId(project.getId());
        buildUI();
        Notification.show("Task dates updated", 2000, Notification.Position.BOTTOM_STRETCH)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
