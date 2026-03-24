package com.example.specdriven.project;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
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
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("projects")
@RouteAlias("")
@PageTitle("Dashboard — Forge")
@PermitAll
public class ProjectDashboardView extends VerticalLayout {

    private final ProjectService projectService;
    private final Div cardGrid = new Div();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public ProjectDashboardView(ProjectService projectService) {
        this.projectService = projectService;
        setPadding(false);
        setSpacing(false);

        Div header = new Div();
        header.addClassName("page-header");

        H2 title = new H2("Projects");

        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(FlexComponent.Alignment.CENTER);

        if (isAdmin()) {
            Button newProjectBtn = new Button("New Project", e -> openCreateDialog());
            newProjectBtn.addThemeVariants(ButtonVariant.PRIMARY);
            actions.add(newProjectBtn);
        }

        header.add(title, actions);
        cardGrid.addClassName("dashboard-grid");

        add(header, cardGrid);
        refreshProjects();
    }

    private void refreshProjects() {
        cardGrid.removeAll();
        List<Project> projects = projectService.findAll();
        for (Project project : projects) {
            cardGrid.add(createProjectCard(project));
        }
    }

    private Div createProjectCard(Project project) {
        Div card = new Div();
        card.addClassName("project-card");

        H3 name = new H3(project.getName());

        Span dateRange = new Span(project.getStartDate().format(DATE_FMT)
                + " — " + project.getEndDate().format(DATE_FMT));
        dateRange.addClassName("date-range");

        int progress = project.getProgressPercent();
        ProjectStatus status = project.getStatus();

        Div progressContainer = new Div();
        progressContainer.addClassName("progress-bar-container");
        Div progressFill = new Div();
        progressFill.addClassName("progress-bar-fill");
        progressFill.addClassName(switch (status) {
            case NOT_STARTED -> "not-started";
            case IN_PROGRESS -> "in-progress";
            case COMPLETED -> "completed";
        });
        progressFill.getStyle().set("width", progress + "%");
        progressContainer.add(progressFill);

        HorizontalLayout bottomRow = new HorizontalLayout();
        bottomRow.setWidthFull();
        bottomRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        bottomRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Span statusBadge = createStatusBadge(status);
        Span progressText = new Span(progress + "% complete");
        progressText.getStyle().set("color", "#757575").set("font-size", "var(--aura-font-size-s)");

        HorizontalLayout statusAndProgress = new HorizontalLayout(statusBadge, progressText);
        statusAndProgress.setAlignItems(FlexComponent.Alignment.CENTER);
        statusAndProgress.setSpacing(true);

        bottomRow.add(statusAndProgress);

        if (isAdmin()) {
            Button deleteBtn = new Button("Delete", e -> {
                e.getSource().getElement().executeJs("event.stopPropagation()");
                ConfirmDialog confirm = new ConfirmDialog();
                confirm.setHeader("Delete project");
                confirm.setText("Are you sure you want to delete \"" + project.getName()
                        + "\"? This will also delete all tasks.");
                confirm.setCancelable(true);
                confirm.setConfirmText("Delete");
                confirm.setConfirmButtonTheme("error primary");
                confirm.addConfirmListener(ev -> {
                    projectService.delete(project.getId());
                    refreshProjects();
                    Notification.show("Project deleted", 3000, Notification.Position.BOTTOM_STRETCH)
                            .addThemeVariants(NotificationVariant.SUCCESS);
                });
                confirm.open();
            });
            deleteBtn.addThemeVariants(ButtonVariant.TERTIARY, ButtonVariant.ERROR);
            deleteBtn.getStyle().set("font-size", "var(--aura-font-size-s)");
            bottomRow.add(deleteBtn);
        }

        card.add(name, dateRange, progressContainer, bottomRow);
        card.addClickListener(e ->
            card.getUI().ifPresent(ui ->
                ui.navigate("projects/" + project.getId() + "/tasks")));

        return card;
    }

    private Span createStatusBadge(ProjectStatus status) {
        Span badge = new Span(status.name().replace('_', ' '));
        badge.addClassName(switch (status) {
            case NOT_STARTED -> "badge-not-started";
            case IN_PROGRESS -> "badge-in-progress";
            case COMPLETED -> "badge-completed";
        });
        return badge;
    }

    private void openCreateDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("New Project");
        dialog.setWidth("480px");

        TextField nameField = new TextField("Name");
        nameField.setWidthFull();
        nameField.setRequired(true);

        TextArea descField = new TextArea("Description");
        descField.setWidthFull();

        DatePicker startDate = new DatePicker("Start Date");
        startDate.setRequired(true);

        DatePicker endDate = new DatePicker("End Date");
        endDate.setRequired(true);

        HorizontalLayout dates = new HorizontalLayout(startDate, endDate);
        dates.setWidthFull();

        VerticalLayout formLayout = new VerticalLayout(nameField, descField, dates);
        formLayout.setPadding(false);
        formLayout.setSpacing(true);
        dialog.add(formLayout);

        Button saveBtn = new Button("Save", e -> {
            try {
                Project project = new Project(
                        nameField.getValue(),
                        descField.getValue(),
                        startDate.getValue(),
                        endDate.getValue());
                projectService.save(project);
                dialog.close();
                refreshProjects();
                Notification.show("Project created", 3000, Notification.Position.BOTTOM_STRETCH)
                        .addThemeVariants(NotificationVariant.SUCCESS);
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage(), 3000, Notification.Position.BOTTOM_STRETCH)
                        .addThemeVariants(NotificationVariant.ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelBtn, saveBtn);

        dialog.open();
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
