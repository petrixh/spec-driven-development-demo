package com.example.specdriven.task;

import com.example.specdriven.project.Project;
import com.example.specdriven.project.ProjectService;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Route("projects/:projectId/gantt")
@PageTitle("Gantt Chart — Forge")
@PermitAll
public class GanttChartView extends VerticalLayout {

    private final TaskService taskService;
    private final ProjectService projectService;

    private Project project;
    private List<Task> tasks;
    private String zoomLevel = "day";
    private static final int CELL_WIDTH_DAY = 40;
    private static final int CELL_WIDTH_WEEK = 20;
    private static final int CELL_WIDTH_MONTH = 6;
    private static final int ROW_HEIGHT = 40;
    private static final DateTimeFormatter HEADER_DAY = DateTimeFormatter.ofPattern("d");
    private static final DateTimeFormatter HEADER_MONTH = DateTimeFormatter.ofPattern("MMM");

    public GanttChartView(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }

    @Override
    protected void onAttach(com.vaadin.flow.component.AttachEvent attachEvent) {
        String path = attachEvent.getUI().getInternals().getActiveViewLocation().getPath();
        String[] parts = path.split("/");
        if (parts.length >= 2) {
            try {
                Long projectId = Long.parseLong(parts[1]);
                this.project = projectService.findById(projectId);
                this.tasks = taskService.findByProjectIdWithDependencies(projectId);
                buildUI();
            } catch (IllegalArgumentException e) {
                add(new H2("Project not found"));
            }
        }
    }

    private void buildUI() {
        removeAll();

        // Toolbar
        Div toolbar = new Div();
        toolbar.addClassName("gantt-toolbar");

        H2 title = new H2(project.getName() + " — Gantt");
        title.getStyle().set("font-size", "var(--aura-font-size-l)").set("margin", "0");

        Button dayBtn = new Button("Day", e -> { zoomLevel = "day"; buildUI(); });
        Button weekBtn = new Button("Week", e -> { zoomLevel = "week"; buildUI(); });
        Button monthBtn = new Button("Month", e -> { zoomLevel = "month"; buildUI(); });

        if ("day".equals(zoomLevel)) dayBtn.addThemeVariants(ButtonVariant.PRIMARY);
        if ("week".equals(zoomLevel)) weekBtn.addThemeVariants(ButtonVariant.PRIMARY);
        if ("month".equals(zoomLevel)) monthBtn.addThemeVariants(ButtonVariant.PRIMARY);

        Button backBtn = new Button("Back to Tasks", e ->
                getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/tasks")));
        backBtn.addThemeVariants(ButtonVariant.TERTIARY);

        HorizontalLayout zoomControls = new HorizontalLayout(dayBtn, weekBtn, monthBtn);
        zoomControls.setSpacing(true);

        toolbar.add(title, zoomControls, backBtn);

        // Gantt body
        Div ganttBody = new Div();
        ganttBody.addClassName("gantt-body");
        ganttBody.getStyle().set("flex", "1").set("min-height", "0");

        // Task list (left)
        Div taskList = new Div();
        taskList.addClassName("gantt-task-list");
        for (Task task : tasks) {
            Div row = new Div();
            row.addClassName("task-row");
            row.setText(task.getName());
            row.setTitle(task.getName());
            taskList.add(row);
        }

        // Timeline (right)
        Div timeline = new Div();
        timeline.addClassName("gantt-timeline");

        LocalDate timelineStart = project.getStartDate();
        LocalDate timelineEnd = project.getEndDate();
        long totalDays = ChronoUnit.DAYS.between(timelineStart, timelineEnd) + 1;

        int cellWidth = getCellWidth();
        int timelineWidth = (int) (totalDays * cellWidth);

        // Header
        Div header = new Div();
        header.addClassName("gantt-header");
        header.getStyle().set("width", timelineWidth + "px");

        for (long d = 0; d < totalDays; d++) {
            LocalDate date = timelineStart.plusDays(d);
            Div cell = new Div();
            cell.addClassName("gantt-header-cell");
            cell.getStyle().set("width", cellWidth + "px").set("min-width", cellWidth + "px");

            if ("day".equals(zoomLevel)) {
                cell.setText(date.format(HEADER_DAY));
            } else if ("week".equals(zoomLevel)) {
                if (date.getDayOfWeek().getValue() == 1 || d == 0) {
                    cell.setText(date.format(HEADER_DAY));
                }
            } else {
                if (date.getDayOfMonth() == 1 || d == 0) {
                    cell.setText(date.format(HEADER_MONTH));
                }
            }

            header.add(cell);
        }

        // Rows with bars
        Div rows = new Div();
        rows.addClassName("gantt-rows");
        rows.getStyle().set("width", timelineWidth + "px").set("position", "relative");

        boolean admin = isAdmin();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            Div row = new Div();
            row.addClassName("gantt-row");

            long startOffset = ChronoUnit.DAYS.between(timelineStart, task.getStartDate());
            long duration = ChronoUnit.DAYS.between(task.getStartDate(), task.getEndDate()) + 1;

            Div bar = new Div();
            bar.addClassName("gantt-bar");
            bar.addClassName(switch (task.getStatus()) {
                case TODO -> "todo";
                case IN_PROGRESS -> "in-progress";
                case DONE -> "done";
            });

            int left = (int) (startOffset * cellWidth);
            int width = (int) (duration * cellWidth);
            bar.getStyle()
                    .set("left", left + "px")
                    .set("width", width + "px");

            bar.setText(task.getName());
            bar.setTitle(task.getName() + " (" + task.getStartDate() + " → " + task.getEndDate() + ")");

            if (admin) {
                bar.addClassName("draggable");
                bar.getElement().setAttribute("data-task-id", String.valueOf(task.getId()));
                bar.getElement().setAttribute("data-task-index", String.valueOf(i));
                bar.getElement().setAttribute("data-start-offset", String.valueOf(startOffset));
                bar.getElement().setAttribute("data-duration", String.valueOf(duration));
                bar.getElement().setAttribute("data-cell-width", String.valueOf(cellWidth));
                bar.getElement().setAttribute("data-project-days", String.valueOf(totalDays));

                // Resize handle
                Div handle = new Div();
                handle.addClassName("resize-handle");
                bar.add(handle);
            }

            bar.addClickListener(e ->
                    getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/tasks")));

            row.add(bar);
            rows.add(row);
        }

        // Today marker
        long todayOffset = ChronoUnit.DAYS.between(timelineStart, LocalDate.now());
        if (todayOffset >= 0 && todayOffset <= totalDays) {
            Div todayMarker = new Div();
            todayMarker.addClassName("gantt-today-marker");
            todayMarker.getStyle().set("left", (todayOffset * cellWidth) + "px");
            rows.add(todayMarker);
        }

        // Dependency arrows (SVG)
        if (!tasks.isEmpty()) {
            renderDependencyArrows(rows, timelineStart, cellWidth);
        }

        timeline.add(header, rows);
        ganttBody.add(taskList, timeline);

        add(toolbar, ganttBody);
        setFlexGrow(1, ganttBody);

        // Add drag/resize JavaScript for admin
        if (admin) {
            setupDragAndResize();
        }
    }

    private void renderDependencyArrows(Div rows, LocalDate timelineStart, int cellWidth) {
        for (int i = 0; i < tasks.size(); i++) {
            Task successor = tasks.get(i);
            for (TaskDependency dep : successor.getPredecessors()) {
                Task predecessor = dep.getPredecessor();
                int predIndex = findTaskIndex(predecessor.getId());
                if (predIndex < 0) continue;

                long predEndOffset = ChronoUnit.DAYS.between(timelineStart, predecessor.getEndDate()) + 1;
                long succStartOffset = ChronoUnit.DAYS.between(timelineStart, successor.getStartDate());

                int x1 = (int) (predEndOffset * cellWidth);
                int y1 = predIndex * ROW_HEIGHT + ROW_HEIGHT / 2;
                int x2 = (int) (succStartOffset * cellWidth);
                int y2 = i * ROW_HEIGHT + ROW_HEIGHT / 2;

                // Create SVG arrow
                int svgWidth = Math.abs(x2 - x1) + 20;
                int svgHeight = Math.abs(y2 - y1) + 20;
                int minX = Math.min(x1, x2) - 10;
                int minY = Math.min(y1, y2) - 10;

                String path = String.format("M%d,%d L%d,%d", x1 - minX, y1 - minY, x2 - minX, y2 - minY);

                Div arrow = new Div();
                arrow.addClassName("gantt-dependency-arrow");
                arrow.getStyle()
                        .set("left", minX + "px")
                        .set("top", minY + "px")
                        .set("width", svgWidth + "px")
                        .set("height", svgHeight + "px");

                arrow.getElement().setProperty("innerHTML",
                        "<svg width='" + svgWidth + "' height='" + svgHeight + "' xmlns='http://www.w3.org/2000/svg'>"
                                + "<defs><marker id='arrowhead' markerWidth='6' markerHeight='4' refX='6' refY='2' orient='auto'>"
                                + "<polygon points='0 0, 6 2, 0 4' fill='#757575'/></marker></defs>"
                                + "<path d='" + path + "' stroke='#757575' stroke-width='1.5' fill='none' marker-end='url(#arrowhead)'/>"
                                + "</svg>");

                rows.add(arrow);
            }
        }
    }

    private int findTaskIndex(Long taskId) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(taskId)) return i;
        }
        return -1;
    }

    private int getCellWidth() {
        return switch (zoomLevel) {
            case "week" -> CELL_WIDTH_WEEK;
            case "month" -> CELL_WIDTH_MONTH;
            default -> CELL_WIDTH_DAY;
        };
    }

    private void setupDragAndResize() {
        getElement().executeJs("""
            const bars = this.querySelectorAll('.gantt-bar.draggable');
            bars.forEach(bar => {
                let isDragging = false;
                let isResizing = false;
                let startX = 0;
                let origLeft = 0;
                let origWidth = 0;
                const cellWidth = parseInt(bar.dataset.cellWidth);
                const projectDays = parseInt(bar.dataset.projectDays);
                const handle = bar.querySelector('.resize-handle');

                if (handle) {
                    handle.addEventListener('mousedown', (e) => {
                        e.stopPropagation();
                        isResizing = true;
                        startX = e.clientX;
                        origWidth = bar.offsetWidth;
                        document.addEventListener('mousemove', onMouseMove);
                        document.addEventListener('mouseup', onMouseUp);
                    });
                }

                bar.addEventListener('mousedown', (e) => {
                    if (isResizing) return;
                    isDragging = true;
                    startX = e.clientX;
                    origLeft = parseInt(bar.style.left);
                    document.addEventListener('mousemove', onMouseMove);
                    document.addEventListener('mouseup', onMouseUp);
                });

                function onMouseMove(e) {
                    const dx = e.clientX - startX;
                    if (isDragging) {
                        let newLeft = origLeft + dx;
                        const barWidth = bar.offsetWidth;
                        newLeft = Math.max(0, Math.min(newLeft, projectDays * cellWidth - barWidth));
                        bar.style.left = newLeft + 'px';
                    } else if (isResizing) {
                        let newWidth = origWidth + dx;
                        newWidth = Math.max(cellWidth, newWidth);
                        const left = parseInt(bar.style.left);
                        if (left + newWidth > projectDays * cellWidth) {
                            newWidth = projectDays * cellWidth - left;
                        }
                        bar.style.width = newWidth + 'px';
                    }
                }

                function onMouseUp(e) {
                    document.removeEventListener('mousemove', onMouseMove);
                    document.removeEventListener('mouseup', onMouseUp);
                    if (isDragging) {
                        isDragging = false;
                        const newLeft = parseInt(bar.style.left);
                        const dayOffset = Math.round(newLeft / cellWidth);
                        const duration = parseInt(bar.dataset.duration);
                        bar.closest('vaadin-vertical-layout')
                            .$server.onTaskDragged(
                                parseInt(bar.dataset.taskId), dayOffset, duration);
                    } else if (isResizing) {
                        isResizing = false;
                        const left = parseInt(bar.style.left);
                        const dayOffset = Math.round(left / cellWidth);
                        const newWidth = parseInt(bar.style.width) || bar.offsetWidth;
                        const newDuration = Math.max(1, Math.round(newWidth / cellWidth));
                        bar.closest('vaadin-vertical-layout')
                            .$server.onTaskDragged(
                                parseInt(bar.dataset.taskId), dayOffset, newDuration);
                    }
                }
            });
        """);
    }

    @ClientCallable
    public void onTaskDragged(int taskId, int dayOffset, int duration) {
        if (!isAdmin()) return;

        try {
            LocalDate newStart = project.getStartDate().plusDays(dayOffset);
            LocalDate newEnd = newStart.plusDays(duration - 1);
            taskService.updateDates((long) taskId, newStart, newEnd);
            // Refresh data
            this.tasks = taskService.findByProjectIdWithDependencies(project.getId());
            buildUI();
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.ERROR);
            buildUI(); // Reset to original positions
        }
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
