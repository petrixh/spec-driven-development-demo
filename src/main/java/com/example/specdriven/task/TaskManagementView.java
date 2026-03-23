package com.example.specdriven.task;

import com.example.specdriven.data.Member;
import com.example.specdriven.data.Project;
import com.example.specdriven.data.Task;
import com.example.specdriven.data.TaskPriority;
import com.example.specdriven.data.TaskStatus;
import com.example.specdriven.project.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("projects/:projectId/tasks")
@PageTitle("Tasks — TickTask")
@PermitAll
public class TaskManagementView extends VerticalLayout implements BeforeEnterObserver {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final AuthenticationContext authContext;

    private Project project;
    private Grid<Task> grid;
    private Select<TaskStatus> statusFilter;
    private ComboBox<Member> assigneeFilter;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public TaskManagementView(TaskService taskService, ProjectService projectService,
                              AuthenticationContext authContext) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.authContext = authContext;
        setPadding(true);
        setSpacing(true);
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
        buildUI();
    }

    private void buildUI() {
        removeAll();

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H2 title = new H2(project.getName() + " — Tasks");
        title.getStyle().set("margin", "0");

        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(FlexComponent.Alignment.CENTER);

        if (authContext.hasRole("ADMIN")) {
            Button addTaskBtn = new Button("Add Task", e -> openTaskDialog(null));
            addTaskBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            actions.add(addTaskBtn);
        }

        Button ganttBtn = new Button("Gantt Chart", e ->
                getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/gantt")));
        actions.add(ganttBtn);

        Button backBtn = new Button("Back to Projects", e ->
                getUI().ifPresent(ui -> ui.navigate("projects")));
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        actions.add(backBtn);

        header.add(title, actions);
        add(header);

        // Filters
        HorizontalLayout filters = new HorizontalLayout();
        filters.setAlignItems(FlexComponent.Alignment.END);

        statusFilter = new Select<>();
        statusFilter.setLabel("Status");
        statusFilter.setItems(TaskStatus.values());
        statusFilter.setEmptySelectionAllowed(true);
        statusFilter.setEmptySelectionCaption("All");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        assigneeFilter = new ComboBox<>("Assignee");
        assigneeFilter.setItems(taskService.findAllMembers());
        assigneeFilter.setItemLabelGenerator(Member::getName);
        assigneeFilter.setClearButtonVisible(true);
        assigneeFilter.addValueChangeListener(e -> refreshGrid());

        filters.add(statusFilter, assigneeFilter);
        add(filters);

        // Grid
        grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeight("600px");

        grid.addColumn(Task::getName).setHeader("Name").setSortable(true).setFlexGrow(2);
        grid.addColumn(t -> t.getAssignee() != null ? t.getAssignee().getName() : "—")
                .setHeader("Assignee").setSortable(true).setFlexGrow(1);
        grid.addColumn(t -> t.getStartDate().format(DATE_FMT))
                .setHeader("Start Date").setSortable(true);
        grid.addColumn(t -> t.getEndDate().format(DATE_FMT))
                .setHeader("End Date").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(task -> {
            Span badge = new Span(task.getPriority().name());
            badge.addClassName("badge");
            switch (task.getPriority()) {
                case LOW -> badge.addClassName("badge-low");
                case MEDIUM -> badge.addClassName("badge-medium");
                case HIGH -> badge.addClassName("badge-high");
            }
            return badge;
        })).setHeader("Priority").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(task -> {
            Span badge = new Span(task.getStatus().name().replace("_", " "));
            badge.addClassName("badge");
            switch (task.getStatus()) {
                case TODO -> badge.addClassName("badge-todo");
                case IN_PROGRESS -> badge.addClassName("badge-in-progress");
                case DONE -> badge.addClassName("badge-done");
            }
            return badge;
        })).setHeader("Status").setSortable(true);

        grid.addItemClickListener(e -> openTaskDialog(e.getItem()));

        add(grid);
        refreshGrid();
    }

    private void refreshGrid() {
        List<Task> tasks = taskService.findByProjectId(project.getId());

        TaskStatus statusValue = statusFilter.getValue();
        if (statusValue != null) {
            tasks = tasks.stream().filter(t -> t.getStatus() == statusValue).toList();
        }

        Member assigneeValue = assigneeFilter.getValue();
        if (assigneeValue != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(assigneeValue.getId()))
                    .toList();
        }

        grid.setItems(tasks);
    }

    private void openTaskDialog(Task existingTask) {
        boolean isNew = (existingTask == null);
        boolean isAdmin = authContext.hasRole("ADMIN");

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(isNew ? "Add Task" : "Edit Task");
        dialog.setWidth("550px");

        FormLayout form = new FormLayout();

        TextField nameField = new TextField("Name");
        nameField.setRequired(true);
        nameField.setWidthFull();
        nameField.setReadOnly(!isAdmin && isNew);

        TextArea descField = new TextArea("Description");
        descField.setWidthFull();
        descField.setReadOnly(!isAdmin && isNew);

        DatePicker startDate = new DatePicker("Start Date");
        startDate.setRequired(true);
        startDate.setReadOnly(!isAdmin);

        DatePicker endDate = new DatePicker("End Date");
        endDate.setRequired(true);
        endDate.setReadOnly(!isAdmin);

        Select<TaskPriority> priorityField = new Select<>();
        priorityField.setLabel("Priority");
        priorityField.setItems(TaskPriority.values());
        priorityField.setReadOnly(!isAdmin);

        Select<TaskStatus> statusField = new Select<>();
        statusField.setLabel("Status");
        statusField.setItems(TaskStatus.values());
        // Any authenticated user can change status

        ComboBox<Member> assigneeField = new ComboBox<>("Assignee");
        assigneeField.setItems(taskService.findAllMembers());
        assigneeField.setItemLabelGenerator(Member::getName);
        assigneeField.setClearButtonVisible(true);
        assigneeField.setReadOnly(!isAdmin);

        if (!isNew) {
            nameField.setValue(existingTask.getName());
            descField.setValue(existingTask.getDescription() != null ? existingTask.getDescription() : "");
            startDate.setValue(existingTask.getStartDate());
            endDate.setValue(existingTask.getEndDate());
            priorityField.setValue(existingTask.getPriority());
            statusField.setValue(existingTask.getStatus());
            assigneeField.setValue(existingTask.getAssignee());
        } else {
            priorityField.setValue(TaskPriority.MEDIUM);
            statusField.setValue(TaskStatus.TODO);
        }

        form.add(nameField, descField, startDate, endDate, priorityField, statusField, assigneeField);

        Button saveBtn = new Button("Save", e -> {
            // Validation
            if (nameField.getValue().isBlank()) {
                nameField.setInvalid(true);
                nameField.setErrorMessage("Name is required");
                return;
            }
            if (startDate.getValue() == null) {
                startDate.setInvalid(true);
                startDate.setErrorMessage("Start date is required");
                return;
            }
            if (endDate.getValue() == null) {
                endDate.setInvalid(true);
                endDate.setErrorMessage("End date is required");
                return;
            }
            if (endDate.getValue().isBefore(startDate.getValue())) {
                endDate.setInvalid(true);
                endDate.setErrorMessage("End date must be on or after start date");
                return;
            }
            // BR-02: Task dates within project range
            if (startDate.getValue().isBefore(project.getStartDate())) {
                startDate.setInvalid(true);
                startDate.setErrorMessage("Start date must not be before project start (" + project.getStartDate() + ")");
                return;
            }
            if (endDate.getValue().isAfter(project.getEndDate())) {
                endDate.setInvalid(true);
                endDate.setErrorMessage("End date must not be after project end (" + project.getEndDate() + ")");
                return;
            }

            Task task = isNew ? new Task() : existingTask;
            if (isAdmin || isNew) {
                task.setName(nameField.getValue().trim());
                task.setDescription(descField.getValue());
                task.setStartDate(startDate.getValue());
                task.setEndDate(endDate.getValue());
                task.setPriority(priorityField.getValue());
                task.setAssignee(assigneeField.getValue());
            }
            task.setStatus(statusField.getValue());
            if (isNew) {
                task.setProject(project);
            }

            taskService.save(task);
            dialog.close();
            refreshGrid();
            Notification.show(isNew ? "Task created" : "Task updated", 3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout rightBtns = new HorizontalLayout(cancelBtn, saveBtn);

        if (!isNew && isAdmin) {
            Button deleteBtn = new Button("Delete", e -> {
                dialog.close();
                ConfirmDialog confirm = new ConfirmDialog();
                confirm.setHeader("Delete Task");
                confirm.setText("Are you sure you want to delete \"" + existingTask.getName() + "\"?");
                confirm.setCancelable(true);
                confirm.setConfirmText("Delete");
                confirm.setConfirmButtonTheme("error primary");
                confirm.addConfirmListener(ev -> {
                    taskService.delete(existingTask.getId());
                    refreshGrid();
                    Notification.show("Task deleted", 3000, Notification.Position.BOTTOM_STRETCH)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                });
                confirm.open();
            });
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            footer.add(deleteBtn, rightBtns);
        } else {
            footer.add(new Span(), rightBtns);
        }

        dialog.add(form);
        dialog.getFooter().add(footer);
        dialog.open();
    }
}
