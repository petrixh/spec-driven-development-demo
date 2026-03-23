package com.example.specdriven.team;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("team")
@PageTitle("Team Workload — TickTask")
@PermitAll
public class TeamWorkloadView extends VerticalLayout {

    public TeamWorkloadView() {
        add(new H2("Team Workload"));
        // Placeholder — implemented in UC-004
    }
}
