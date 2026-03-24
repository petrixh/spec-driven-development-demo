package com.example.specdriven.task;

import com.example.specdriven.member.Member;
import com.example.specdriven.member.MemberService;
import com.example.specdriven.project.Project;
import com.example.specdriven.project.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("projects/:projectId/tasks")
@PageTitle("Tasks — Forge")
@PermitAll
public class TaskManagementView extends VerticalLayout {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final MemberService memberService;

    private Project project;
    private final Grid<Task> grid = new Grid<>();
    private final VerticalLayout editPanel = new VerticalLayout();
    private Task selectedTask;

    private Select<TaskStatus> statusFilter;
    private ComboBox<Member> assigneeFilter;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d");

    public TaskManagementView(TaskService taskService, ProjectService projectService,
                              MemberService memberService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.memberService = memberService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }

    @Override
    protected void onAttach(com.vaadin.flow.component.AttachEvent attachEvent) {
        // Extract projectId from URL
        String path = attachEvent.getUI().getInternals().getActiveViewLocation().getPath();
        String[] parts = path.split("/");
        // path: projects/{projectId}/tasks
        if (parts.length >= 2) {
            try {
                Long projectId = Long.parseLong(parts[1]);
                this.project = projectService.findById(projectId);
                buildUI();
            } catch (IllegalArgumentException e) {
                add(new H2("Project not found"));
            }
        }
    }

    private void buildUI() {
        // Header
        Div header = new Div();
        header.addClassName("page-header");

        H2 title = new H2(project.getName() + " — Tasks");

        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.setSpacing(true);

        if (isAdmin()) {
            Button addTaskBtn = new Button("Add Task", e -> openEditPanel(null));
            addTaskBtn.addThemeVariants(ButtonVariant.PRIMARY);
            actions.add(addTaskBtn);
        }

        Button ganttBtn = new Button("Gantt Chart", e ->
                getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/gantt")));
        actions.add(ganttBtn);

        Button backBtn = new Button("Back to Projects", e ->
                getUI().ifPresent(ui -> ui.navigate("projects")));
        backBtn.addThemeVariants(ButtonVariant.TERTIARY);
        actions.add(backBtn);

        header.add(title, actions);

        // Filters
        HorizontalLayout filters = new HorizontalLayout();
        filters.setPadding(true);
        filters.setSpacing(true);
        filters.setAlignItems(FlexComponent.Alignment.END);

        statusFilter = new Select<>();
        statusFilter.setLabel("Status");
        statusFilter.setItems(TaskStatus.values());
        statusFilter.setEmptySelectionAllowed(true);
        statusFilter.setEmptySelectionCaption("All");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        assigneeFilter = new ComboBox<>("Assignee");
        assigneeFilter.setItems(memberService.findAll());
        assigneeFilter.setItemLabelGenerator(Member::getName);
        assigneeFilter.setClearButtonVisible(true);
        assigneeFilter.addValueChangeListener(e -> refreshGrid());

        filters.add(statusFilter, assigneeFilter);

        // Grid setup
        setupGrid();

        // Split layout: grid + edit panel
        VerticalLayout gridSection = new VerticalLayout(filters, grid);
        gridSection.setPadding(false);
        gridSection.setSpacing(false);
        gridSection.setSizeFull();
        grid.setSizeFull();

        editPanel.setVisible(false);
        editPanel.setWidth("400px");
        editPanel.setPadding(true);

        SplitLayout splitLayout = new SplitLayout(gridSection, editPanel);
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(70);

        add(header, splitLayout);
        setFlexGrow(1, splitLayout);

        refreshGrid();
    }

    private void setupGrid() {
        grid.addColumn(Task::getName).setHeader("Name").setSortable(true).setFlexGrow(2);

        grid.addColumn(t -> t.getAssignee() != null ? t.getAssignee().getName() : "—")
                .setHeader("Assignee").setSortable(true);

        grid.addColumn(t -> t.getStartDate().format(DATE_FMT))
                .setHeader("Start").setSortable(true);

        grid.addColumn(t -> t.getEndDate().format(DATE_FMT))
                .setHeader("End").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(task -> {
            Span badge = new Span(task.getPriority().name());
            badge.addClassName("badge-" + task.getPriority().name().toLowerCase());
            return badge;
        })).setHeader("Priority").setSortable(true)
                .setComparator((a, b) -> a.getPriority().compareTo(b.getPriority()));

        grid.addColumn(new ComponentRenderer<>(task -> {
            String label = task.getStatus().name().replace('_', ' ');
            Span badge = new Span(label);
            badge.addClassName("badge-" + task.getStatus().name().toLowerCase().replace('_', '-'));
            return badge;
        })).setHeader("Status").setSortable(true)
                .setComparator((a, b) -> a.getStatus().compareTo(b.getStatus()));

        grid.addItemClickListener(e -> openEditPanel(e.getItem()));
    }

    private void refreshGrid() {
        List<Task> tasks = taskService.findByProjectId(project.getId());

        TaskStatus statusVal = statusFilter != null ? statusFilter.getValue() : null;
        Member assigneeVal = assigneeFilter != null ? assigneeFilter.getValue() : null;

        if (statusVal != null) {
            tasks = tasks.stream().filter(t -> t.getStatus() == statusVal).toList();
        }
        if (assigneeVal != null) {
            tasks = tasks.stream().filter(t ->
                    t.getAssignee() != null && t.getAssignee().getId().equals(assigneeVal.getId())).toList();
        }

        grid.setItems(tasks);
    }

    private void openEditPanel(Task task) {
        editPanel.removeAll();
        editPanel.setVisible(true);

        boolean isNew = (task == null);
        selectedTask = isNew ? new Task() : task;

        H2 panelTitle = new H2(isNew ? "New Task" : "Edit Task");

        TextField nameField = new TextField("Name");
        nameField.setWidthFull();
        nameField.setValue(selectedTask.getName() != null ? selectedTask.getName() : "");

        TextArea descField = new TextArea("Description");
        descField.setWidthFull();
        descField.setValue(selectedTask.getDescription() != null ? selectedTask.getDescription() : "");

        DatePicker startDate = new DatePicker("Start Date");
        startDate.setValue(selectedTask.getStartDate());

        DatePicker endDate = new DatePicker("End Date");
        endDate.setValue(selectedTask.getEndDate());

        Select<TaskPriority> prioritySelect = new Select<>();
        prioritySelect.setLabel("Priority");
        prioritySelect.setItems(TaskPriority.values());
        prioritySelect.setValue(selectedTask.getPriority() != null ? selectedTask.getPriority() : TaskPriority.MEDIUM);

        Select<TaskStatus> statusSelect = new Select<>();
        statusSelect.setLabel("Status");
        statusSelect.setItems(TaskStatus.values());
        statusSelect.setValue(selectedTask.getStatus() != null ? selectedTask.getStatus() : TaskStatus.TODO);

        ComboBox<Member> assigneeSelect = new ComboBox<>("Assignee");
        assigneeSelect.setItems(memberService.findAll());
        assigneeSelect.setItemLabelGenerator(Member::getName);
        assigneeSelect.setClearButtonVisible(true);
        if (selectedTask.getAssignee() != null) {
            assigneeSelect.setValue(selectedTask.getAssignee());
        }

        Button saveBtn = new Button("Save", e -> {
            try {
                selectedTask.setName(nameField.getValue());
                selectedTask.setDescription(descField.getValue());
                selectedTask.setStartDate(startDate.getValue());
                selectedTask.setEndDate(endDate.getValue());
                selectedTask.setPriority(prioritySelect.getValue());
                selectedTask.setStatus(statusSelect.getValue());
                selectedTask.setAssignee(assigneeSelect.getValue());
                if (isNew) {
                    selectedTask.setProject(project);
                }
                taskService.save(selectedTask);
                editPanel.setVisible(false);
                refreshGrid();
                Notification.show("Task saved", 3000, Notification.Position.BOTTOM_STRETCH)
                        .addThemeVariants(NotificationVariant.SUCCESS);
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage(), 3000, Notification.Position.BOTTOM_STRETCH)
                        .addThemeVariants(NotificationVariant.ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> editPanel.setVisible(false));

        HorizontalLayout buttonBar = new HorizontalLayout(saveBtn, cancelBtn);

        editPanel.add(panelTitle, nameField, descField, startDate, endDate,
                prioritySelect, statusSelect, assigneeSelect, buttonBar);

        if (!isNew && isAdmin()) {
            Button deleteBtn = new Button("Delete", e -> {
                ConfirmDialog confirm = new ConfirmDialog();
                confirm.setHeader("Delete task");
                confirm.setText("Are you sure you want to delete \"" + selectedTask.getName() + "\"?");
                confirm.setCancelable(true);
                confirm.setConfirmText("Delete");
                confirm.setConfirmButtonTheme("error primary");
                confirm.addConfirmListener(ev -> {
                    taskService.delete(selectedTask.getId());
                    editPanel.setVisible(false);
                    refreshGrid();
                    Notification.show("Task deleted", 3000, Notification.Position.BOTTOM_STRETCH)
                            .addThemeVariants(NotificationVariant.SUCCESS);
                });
                confirm.open();
            });
            deleteBtn.addThemeVariants(ButtonVariant.ERROR);
            buttonBar.add(deleteBtn);
        }
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
