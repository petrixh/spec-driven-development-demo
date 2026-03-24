package com.example.specdriven.admin;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Route(value = "admin/queue", layout = AdminLayout.class)
@PageTitle("Ticket Queue | re:solve")
@RolesAllowed("ADMIN")
public class QueueView extends VerticalLayout {

    private final TicketService ticketService;
    private final Grid<Ticket> grid = new Grid<>();
    private List<Ticket> allTickets;

    private Select<Status> statusFilter;
    private Select<Category> categoryFilter;
    private Select<Priority> priorityFilter;

    public QueueView(TicketService ticketService) {
        this.ticketService = ticketService;

        addClassName("admin-content");
        setPadding(true);

        add(new H2("Ticket Queue"));

        createFilters();
        createGrid();
        loadTickets();
    }

    private void createFilters() {
        statusFilter = new Select<>();
        statusFilter.setLabel("Status");
        statusFilter.setItems(Status.values());
        statusFilter.setEmptySelectionAllowed(true);
        statusFilter.setEmptySelectionCaption("All");
        statusFilter.addValueChangeListener(e -> applyFilters());

        categoryFilter = new Select<>();
        categoryFilter.setLabel("Category");
        categoryFilter.setItems(Category.values());
        categoryFilter.setEmptySelectionAllowed(true);
        categoryFilter.setEmptySelectionCaption("All");
        categoryFilter.addValueChangeListener(e -> applyFilters());

        priorityFilter = new Select<>();
        priorityFilter.setLabel("Priority");
        priorityFilter.setItems(Priority.values());
        priorityFilter.setEmptySelectionAllowed(true);
        priorityFilter.setEmptySelectionCaption("All");
        priorityFilter.addValueChangeListener(e -> applyFilters());

        HorizontalLayout filters = new HorizontalLayout(statusFilter, categoryFilter, priorityFilter);
        filters.setAlignItems(Alignment.BASELINE);
        add(filters);
    }

    private void createGrid() {
        grid.addColumn(Ticket::getId).setHeader("ID").setSortable(true).setAutoWidth(true);
        grid.addColumn(Ticket::getTitle).setHeader("Title").setSortable(true).setFlexGrow(1);
        grid.addColumn(Ticket::getCategory).setHeader("Category").setSortable(true).setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(ticket -> {
            Span badge = new Span(ticket.getPriority().name());
            badge.addClassNames("badge", "badge-" + ticket.getPriority().name().toLowerCase());
            return badge;
        })).setHeader("Priority").setSortable(true).setComparator(Ticket::getPriority).setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(ticket -> {
            Span badge = new Span(ticket.getStatus().name().replace("_", " "));
            badge.addClassNames("badge", "badge-" + ticket.getStatus().name().toLowerCase().replace("_", "-"));
            return badge;
        })).setHeader("Status").setSortable(true).setComparator(Ticket::getStatus).setAutoWidth(true);
        grid.addColumn(ticket -> ticket.getCreatedBy().getName())
                .setHeader("Created By").setSortable(true).setAutoWidth(true);
        grid.addColumn(ticket -> ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : "Unassigned")
                .setHeader("Assigned To").setSortable(true).setAutoWidth(true);
        grid.addColumn(ticket -> ticket.getCreatedDate() != null
                        ? ticket.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "")
                .setHeader("Created").setSortable(true).setAutoWidth(true);
        grid.addComponentColumn(ticket -> {
            Button assignBtn = new Button("Assign to me", e -> {
                ticketService.assignToMe(ticket.getId());
                loadTickets();
                Notification.show("Ticket assigned to you", 3000, Notification.Position.TOP_CENTER);
            });
            assignBtn.setThemeName("small");
            return assignBtn;
        }).setHeader("Actions").setAutoWidth(true);

        grid.addItemClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("admin/ticket/" + e.getItem().getId()));
        });

        grid.setWidthFull();
        add(grid);
    }

    private void loadTickets() {
        // Default: non-closed tickets
        allTickets = ticketService.getTicketsByStatuses(
                List.of(Status.OPEN, Status.IN_PROGRESS, Status.RESOLVED));
        applyFilters();
    }

    private void applyFilters() {
        if (allTickets == null) return;

        Status selectedStatus = statusFilter.getValue();
        Category selectedCategory = categoryFilter.getValue();
        Priority selectedPriority = priorityFilter.getValue();

        Stream<Ticket> stream;

        // If a specific status is selected, re-fetch to include CLOSED if needed
        if (selectedStatus != null) {
            stream = ticketService.getAllTickets().stream()
                    .filter(t -> t.getStatus() == selectedStatus);
        } else {
            stream = allTickets.stream();
        }

        if (selectedCategory != null) {
            stream = stream.filter(t -> t.getCategory() == selectedCategory);
        }
        if (selectedPriority != null) {
            stream = stream.filter(t -> t.getPriority() == selectedPriority);
        }

        grid.setItems(stream.toList());
    }
}
