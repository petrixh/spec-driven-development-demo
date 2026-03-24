package com.example.specdriven.admin;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.TicketRepository;
import com.example.specdriven.domain.User;
import com.example.specdriven.domain.UserRepository;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ManageTicketQueueTest extends SpringBrowserlessTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        ticketRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void gridDisplaysAllTickets() {
        // Create tickets as customer, then navigate as agent
        User customer = userRepository.findByEmail("customer@test.com").orElseThrow();
        ticketRepository.save(new Ticket("Test 1", "Desc", Category.GENERAL, Priority.LOW, customer));
        ticketRepository.save(new Ticket("Test 2", "Desc", Category.TECHNICAL, Priority.HIGH, customer));

        QueueView view = navigate(QueueView.class);
        Grid<Ticket> grid = $(Grid.class).first();

        assertNotNull(grid);
        assertTrue(grid.getColumns().size() >= 8);
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void defaultFilterExcludesClosedTickets() {
        User customer = userRepository.findByEmail("customer@test.com").orElseThrow();
        Ticket open = ticketRepository.save(new Ticket("Open", "Desc", Category.GENERAL, Priority.LOW, customer));
        Ticket closed = ticketRepository.save(new Ticket("Closed", "Desc", Category.GENERAL, Priority.LOW, customer));
        closed.setStatus(Status.CLOSED);
        ticketRepository.save(closed);

        QueueView view = navigate(QueueView.class);
        Grid<Ticket> grid = $(Grid.class).first();

        var items = grid.getGenericDataView().getItems().toList();
        assertTrue(items.stream().noneMatch(t -> t.getStatus() == Status.CLOSED));
        assertTrue(items.stream().anyMatch(t -> t.getStatus() == Status.OPEN));
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void assignToMeAssignsTicket() {
        User customer = userRepository.findByEmail("customer@test.com").orElseThrow();
        Ticket ticket = ticketRepository.save(new Ticket("Assign test", "Desc", Category.TECHNICAL, Priority.MEDIUM, customer));

        assertNull(ticket.getAssignedTo());

        Ticket assigned = ticketService.assignToMe(ticket.getId());
        assertEquals("agent@test.com", assigned.getAssignedTo().getEmail());
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccessQueue() {
        assertThrows(Exception.class, () -> navigate(QueueView.class));
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void gridSupportsColumnSorting() {
        QueueView view = navigate(QueueView.class);
        Grid<Ticket> grid = $(Grid.class).first();

        grid.getColumns().forEach(col -> {
            // ID, Title, Category, Priority, Status, Created By, Assigned To, Created columns are sortable
            if (!col.getHeaderText().equals("Actions")) {
                assertTrue(col.isSortable(), "Column '" + col.getHeaderText() + "' should be sortable");
            }
        });
    }
}
