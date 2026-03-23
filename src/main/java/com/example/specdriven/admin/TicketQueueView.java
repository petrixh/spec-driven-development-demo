package com.example.specdriven.admin;

import com.example.specdriven.data.*;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "admin/queue", layout = AdminLayout.class)
@PageTitle("Ticket Queue — re:solve")
@RolesAllowed("ADMIN")
public class TicketQueueView extends VerticalLayout {

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final Grid<Ticket> grid = new Grid<>(Ticket.class, false);

    private String statusFilter = "NON_CLOSED";
    private String categoryFilter = "ALL";
    private String priorityFilter = "ALL";

    public TicketQueueView(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;

        setSizeFull();
        setPadding(true);

        add(new H2("Ticket Queue"));
        add(createFilters());
        configureGrid();
        add(grid);
        grid.setSizeFull();

        grid.addItemDoubleClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("admin/ticket/" + e.getItem().getId())));

        refreshGrid();
    }

    private HorizontalLayout createFilters() {
        Select<String> statusSelect = new Select<>();
        statusSelect.setLabel("Status");
        statusSelect.setItems("NON_CLOSED", "ALL", "OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");
        statusSelect.setItemLabelGenerator(s -> s.equals("NON_CLOSED") ? "Non-closed" : s.replace("_", " "));
        statusSelect.setValue("NON_CLOSED");
        statusSelect.addValueChangeListener(e -> { statusFilter = e.getValue(); refreshGrid(); });

        Select<String> categorySelect = new Select<>();
        categorySelect.setLabel("Category");
        categorySelect.setItems("ALL", "GENERAL", "TECHNICAL", "BILLING", "ACCESS");
        categorySelect.setValue("ALL");
        categorySelect.addValueChangeListener(e -> { categoryFilter = e.getValue(); refreshGrid(); });

        Select<String> prioritySelect = new Select<>();
        prioritySelect.setLabel("Priority");
        prioritySelect.setItems("ALL", "LOW", "MEDIUM", "HIGH", "CRITICAL");
        prioritySelect.setValue("ALL");
        prioritySelect.addValueChangeListener(e -> { priorityFilter = e.getValue(); refreshGrid(); });

        HorizontalLayout filters = new HorizontalLayout(statusSelect, categorySelect, prioritySelect);
        filters.setAlignItems(Alignment.BASELINE);
        return filters;
    }

    private void configureGrid() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        grid.addColumn(Ticket::getId).setHeader("ID").setSortable(true).setWidth("80px");
        grid.addColumn(Ticket::getTitle).setHeader("Title").setSortable(true).setFlexGrow(2);
        grid.addColumn(t -> t.getCategory().name()).setHeader("Category").setSortable(true);
        grid.addColumn(t -> t.getPriority().name()).setHeader("Priority").setSortable(true);
        grid.addColumn(t -> t.getStatus().name().replace("_", " ")).setHeader("Status").setSortable(true);
        grid.addColumn(t -> t.getCreatedBy().getName()).setHeader("Created By").setSortable(true);
        grid.addColumn(t -> t.getAssignedTo() != null ? t.getAssignedTo().getName() : "—")
                .setHeader("Assigned To").setSortable(true);
        grid.addColumn(t -> t.getCreatedDate().format(fmt)).setHeader("Created").setSortable(true);
        grid.addComponentColumn(ticket -> {
            Button btn = new Button("Assign to me");
            btn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btn.addClickListener(e -> assignToMe(ticket));
            return btn;
        }).setHeader("Actions").setWidth("140px");
    }

    private void assignToMe(Ticket ticket) {
        User agent = getCurrentUser();
        ticketService.assignTicket(ticket.getId(), agent);
        Notification.show("Assigned to " + agent.getName(), 3000, Notification.Position.TOP_CENTER);
        refreshGrid();
    }

    private void refreshGrid() {
        List<Ticket> tickets;
        if ("NON_CLOSED".equals(statusFilter)) {
            tickets = ticketService.getNonClosedTickets();
        } else if ("ALL".equals(statusFilter)) {
            tickets = ticketService.getAllTickets();
        } else {
            tickets = ticketService.getAllTickets().stream()
                    .filter(t -> t.getStatus().name().equals(statusFilter))
                    .toList();
        }

        if (!"ALL".equals(categoryFilter)) {
            tickets = tickets.stream()
                    .filter(t -> t.getCategory().name().equals(categoryFilter))
                    .toList();
        }
        if (!"ALL".equals(priorityFilter)) {
            tickets = tickets.stream()
                    .filter(t -> t.getPriority().name().equals(priorityFilter))
                    .toList();
        }

        grid.setItems(tickets);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
