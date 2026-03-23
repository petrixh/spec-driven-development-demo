package com.example.specdriven.gantt;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("projects/:projectId/gantt")
@PageTitle("Gantt Chart — TickTask")
@PermitAll
public class GanttChartView extends VerticalLayout implements BeforeEnterObserver {

    public GanttChartView() {
        add(new H2("Gantt Chart"));
        // Placeholder — fully implemented in UC-003
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Will be implemented in UC-003
    }
}
