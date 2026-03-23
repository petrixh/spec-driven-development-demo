package com.example.specdriven.project;

import com.example.specdriven.data.Project;
import com.example.specdriven.data.ProjectStatus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;

@Route("projects")
@PageTitle("Projects — TickTask")
@PermitAll
public class ProjectDashboardView extends VerticalLayout {

    private final ProjectService projectService;
    private final AuthenticationContext authContext;
    private final Div cardGrid = new Div();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public ProjectDashboardView(ProjectService projectService, AuthenticationContext authContext) {
        this.projectService = projectService;
        this.authContext = authContext;

        addClassName("page-content");
        setSpacing(true);
        setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H2 title = new H2("Projects");
        title.getStyle().set("margin", "0");
        header.add(title);

        if (authContext.hasRole("ADMIN")) {
            Button newProjectBtn = new Button("New Project", e -> openCreateDialog());
            newProjectBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            header.add(newProjectBtn);
        }

        add(header);

        cardGrid.addClassName("card-grid");
        add(cardGrid);

        refreshCards();
    }

    private void refreshCards() {
        cardGrid.removeAll();
        for (Project project : projectService.findAll()) {
            cardGrid.add(createProjectCard(project));
        }
    }

    private Div createProjectCard(Project project) {
        Div card = new Div();
        card.addClassName("project-card");

        // Header row with name and delete button
        HorizontalLayout cardHeader = new HorizontalLayout();
        cardHeader.setWidthFull();
        cardHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        cardHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H3 name = new H3(project.getName());
        name.getStyle().set("margin", "0");
        cardHeader.add(name);

        if (authContext.hasRole("ADMIN")) {
            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> {
                e.getSource().getElement().executeJs("event.stopPropagation()");
                confirmDelete(project);
            });
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteBtn.getElement().addEventListener("click", ev -> {}).addEventData("event.stopPropagation()");
            cardHeader.add(deleteBtn);
        }

        card.add(cardHeader);

        // Date range
        String dateRange = project.getStartDate().format(DATE_FMT) + " — " + project.getEndDate().format(DATE_FMT);
        Span dates = new Span(dateRange);
        dates.getStyle().set("color", "#757575").set("font-size", "var(--aura-font-size-s)");
        card.add(dates);

        // Progress bar
        int progress = projectService.calculateProgressPercent(project);
        Div track = new Div();
        track.addClassName("progress-bar-track");
        track.getStyle().set("margin-top", "var(--vaadin-gap-xs)");

        Div fill = new Div();
        fill.addClassName("progress-bar-fill");
        fill.getStyle()
                .set("width", progress + "%")
                .set("background", progress == 100 ? "#2E7D32" : "#F57C00");
        track.add(fill);
        card.add(track);

        // Progress text
        Span progressText = new Span(progress + "% complete");
        progressText.getStyle().set("font-size", "var(--aura-font-size-s)").set("color", "#757575");
        card.add(progressText);

        // Status badge
        ProjectStatus status = projectService.calculateStatus(project);
        Span badge = new Span(status.name().replace("_", " "));
        badge.addClassName("badge");
        switch (status) {
            case NOT_STARTED -> badge.addClassName("badge-not-started");
            case IN_PROGRESS -> badge.addClassName("badge-in-progress");
            case COMPLETED -> badge.addClassName("badge-completed");
        }
        badge.getStyle().set("margin-top", "var(--vaadin-gap-xs)");
        card.add(badge);

        // Click to navigate to tasks
        card.addClickListener(e ->
            card.getUI().ifPresent(ui -> ui.navigate("projects/" + project.getId() + "/tasks"))
        );

        return card;
    }

    private void confirmDelete(Project project) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete Project");
        dialog.setText("Are you sure you want to delete \"" + project.getName() + "\"? This will also delete all its tasks.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            projectService.delete(project.getId());
            refreshCards();
            Notification.show("Project deleted", 3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        dialog.open();
    }

    private void openCreateDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("New Project");
        dialog.setWidth("500px");

        FormLayout form = new FormLayout();

        TextField nameField = new TextField("Name");
        nameField.setRequired(true);
        nameField.setWidthFull();

        TextArea descField = new TextArea("Description");
        descField.setWidthFull();

        DatePicker startDate = new DatePicker("Start Date");
        startDate.setRequired(true);

        DatePicker endDate = new DatePicker("End Date");
        endDate.setRequired(true);

        form.add(nameField, descField, startDate, endDate);

        Button saveBtn = new Button("Save", e -> {
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
            if (!endDate.getValue().isAfter(startDate.getValue())) {
                endDate.setInvalid(true);
                endDate.setErrorMessage("End date must be after start date");
                return;
            }

            Project project = new Project(
                    nameField.getValue().trim(),
                    descField.getValue(),
                    startDate.getValue(),
                    endDate.getValue()
            );
            projectService.save(project);
            dialog.close();
            refreshCards();
            Notification.show("Project created", 3000, Notification.Position.BOTTOM_STRETCH)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

}
