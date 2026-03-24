package com.example.specdriven.ticket;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubmitTicketTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    @WithMockUser(username = "customer@test.com")
    void submitTicketCreatesTicketWithStatusOpen() {
        Ticket ticket = ticketService.submitTicket("Test issue", "Description of the issue",
                Category.TECHNICAL, Priority.MEDIUM);

        assertNotNull(ticket.getId());
        assertEquals("Test issue", ticket.getTitle());
        assertEquals("Description of the issue", ticket.getDescription());
        assertEquals(Category.TECHNICAL, ticket.getCategory());
        assertEquals(Priority.MEDIUM, ticket.getPriority());
        assertEquals(Status.OPEN, ticket.getStatus());
        assertNotNull(ticket.getCreatedBy());
        assertEquals("customer@test.com", ticket.getCreatedBy().getEmail());
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void submitTicketDefaultsPriorityToMedium() {
        Ticket ticket = ticketService.submitTicket("Test", "Description",
                Category.GENERAL, null);

        assertEquals(Priority.MEDIUM, ticket.getPriority());
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void submitTicketRequiresTitle() {
        assertThrows(IllegalArgumentException.class, () ->
                ticketService.submitTicket("", "Description", Category.GENERAL, Priority.LOW));
        assertThrows(IllegalArgumentException.class, () ->
                ticketService.submitTicket(null, "Description", Category.GENERAL, Priority.LOW));
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void submitTicketRequiresDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                ticketService.submitTicket("Title", "", Category.GENERAL, Priority.LOW));
        assertThrows(IllegalArgumentException.class, () ->
                ticketService.submitTicket("Title", null, Category.GENERAL, Priority.LOW));
    }

    @Test
    @WithMockUser(username = "customer@test.com")
    void submitTicketSetsCreatedDate() {
        Ticket ticket = ticketService.submitTicket("Test", "Description",
                Category.BILLING, Priority.HIGH);

        assertNotNull(ticket.getCreatedDate());
    }
}
