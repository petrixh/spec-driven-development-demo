package com.example.specdriven.task;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("projects/:projectId/tasks")
@PageTitle("Tasks — TickTask")
@PermitAll
public class TaskManagementView extends VerticalLayout implements BeforeEnterObserver {

    public TaskManagementView() {
        add(new H2("Task Management"));
        // Placeholder — fully implemented in UC-002
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Will be implemented in UC-002
    }
}
