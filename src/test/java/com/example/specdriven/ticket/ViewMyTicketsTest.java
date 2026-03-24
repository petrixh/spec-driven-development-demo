package com.example.specdriven.ticket;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.User;
import com.example.specdriven.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ViewMyTicketsTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "customer@test.com")
    void customerSeesOnlyTheirOwnTickets() {
        ticketService.submitTicket("Customer ticket", "Description", Category.GENERAL, Priority.LOW);

        List<Ticket> tickets = ticketService.getMyTickets(null);
        assertFalse(tickets.isEmpty());
        tickets.forEach(t ->
                assertEquals("customer@test.com", t.getCreatedBy().getEmail()));
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void filterByStatusWorks() {
        ticketService.submitTicket("Open ticket", "Desc", Category.TECHNICAL, Priority.MEDIUM);

        List<Ticket> openTickets = ticketService.getMyTickets(Status.OPEN);
        assertFalse(openTickets.isEmpty());
        openTickets.forEach(t -> assertEquals(Status.OPEN, t.getStatus()));

        List<Ticket> closedTickets = ticketService.getMyTickets(Status.CLOSED);
        assertTrue(closedTickets.isEmpty());
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void ticketDetailShowsAllFields() {
        Ticket ticket = ticketService.submitTicket("Detail test", "Full description",
                Category.BILLING, Priority.HIGH);

        Ticket detail = ticketService.getTicketById(ticket.getId());

        assertEquals("Detail test", detail.getTitle());
        assertEquals("Full description", detail.getDescription());
        assertEquals(Category.BILLING, detail.getCategory());
        assertEquals(Priority.HIGH, detail.getPriority());
        assertEquals(Status.OPEN, detail.getStatus());
        assertNotNull(detail.getCreatedBy());
        assertNotNull(detail.getCreatedDate());
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void ticketDetailShowsComments() {
        Ticket ticket = ticketService.submitTicket("Comment test", "Desc",
                Category.GENERAL, Priority.LOW);

        var comments = ticketService.getComments(ticket.getId());
        assertTrue(comments.isEmpty());
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void defaultFilterShowsAllStatuses() {
        ticketService.submitTicket("All statuses test", "Desc", Category.ACCESS, Priority.MEDIUM);

        List<Ticket> allTickets = ticketService.getMyTickets(null);
        assertFalse(allTickets.isEmpty());
    }
}
