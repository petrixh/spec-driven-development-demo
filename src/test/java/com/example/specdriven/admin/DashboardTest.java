package com.example.specdriven.admin;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.CommentRepository;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.TicketRepository;
import com.example.specdriven.domain.User;
import com.example.specdriven.domain.UserRepository;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DashboardTest extends SpringBrowserlessTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private User customer;

    @BeforeEach
    void setup() {
        commentRepository.deleteAll();
        ticketRepository.deleteAll();
        customer = userRepository.findByEmail("customer@test.com").orElseThrow();
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void dashboardDisplaysCorrectOpenCount() {
        ticketRepository.save(new Ticket("Open 1", "Desc", Category.GENERAL, Priority.LOW, customer));
        ticketRepository.save(new Ticket("Open 2", "Desc", Category.TECHNICAL, Priority.MEDIUM, customer));

        long openCount = ticketService.countByStatus(Status.OPEN);
        assertEquals(2, openCount);
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void dashboardDisplaysCorrectInProgressCount() {
        Ticket ticket = ticketRepository.save(
                new Ticket("IP ticket", "Desc", Category.BILLING, Priority.HIGH, customer));
        ticketService.changeStatus(ticket.getId(), Status.IN_PROGRESS);

        long ipCount = ticketService.countByStatus(Status.IN_PROGRESS);
        assertEquals(1, ipCount);
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void categoryBreakdownCountsNonClosedTickets() {
        ticketRepository.save(new Ticket("Tech 1", "Desc", Category.TECHNICAL, Priority.LOW, customer));
        ticketRepository.save(new Ticket("Tech 2", "Desc", Category.TECHNICAL, Priority.MEDIUM, customer));
        ticketRepository.save(new Ticket("General", "Desc", Category.GENERAL, Priority.LOW, customer));

        long techCount = ticketService.countNonClosedByCategory(Category.TECHNICAL);
        long generalCount = ticketService.countNonClosedByCategory(Category.GENERAL);

        assertEquals(2, techCount);
        assertEquals(1, generalCount);
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void priorityBreakdownCountsNonClosedTickets() {
        ticketRepository.save(new Ticket("High 1", "Desc", Category.GENERAL, Priority.HIGH, customer));
        ticketRepository.save(new Ticket("High 2", "Desc", Category.GENERAL, Priority.HIGH, customer));
        ticketRepository.save(new Ticket("Low 1", "Desc", Category.GENERAL, Priority.LOW, customer));

        long highCount = ticketService.countNonClosedByPriority(Priority.HIGH);
        long lowCount = ticketService.countNonClosedByPriority(Priority.LOW);

        assertEquals(2, highCount);
        assertEquals(1, lowCount);
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void closedTicketsExcludedFromBreakdowns() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Closed", "Desc", Category.ACCESS, Priority.CRITICAL, customer));
        ticketService.changeStatus(ticket.getId(), Status.IN_PROGRESS);
        ticketService.changeStatus(ticket.getId(), Status.RESOLVED);
        ticketService.changeStatus(ticket.getId(), Status.CLOSED);

        long accessCount = ticketService.countNonClosedByCategory(Category.ACCESS);
        assertEquals(0, accessCount);
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void dashboardViewRendersSuccessfully() {
        DashboardView view = navigate(DashboardView.class);
        assertNotNull(view);
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccessDashboard() {
        assertThrows(Exception.class, () -> navigate(DashboardView.class));
    }
}
