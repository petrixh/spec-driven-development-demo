package com.example.specdriven.admin;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Comment;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.CommentRepository;
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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorkOnTicketTest extends SpringBrowserlessTest {

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
    void addCommentAppearsInHistory() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Comment test", "Desc", Category.GENERAL, Priority.LOW, customer));

        Comment comment = ticketService.addComment(ticket.getId(), "This is a test comment");

        assertNotNull(comment.getId());
        assertEquals("This is a test comment", comment.getText());
        assertEquals("Bob Agent", comment.getAuthor().getName());
        assertNotNull(comment.getCreatedDate());

        List<Comment> comments = ticketService.getComments(ticket.getId());
        assertEquals(1, comments.size());
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void emptyCommentIsPrevented() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Empty comment test", "Desc", Category.GENERAL, Priority.LOW, customer));

        assertThrows(IllegalArgumentException.class,
                () -> ticketService.addComment(ticket.getId(), ""));
        assertThrows(IllegalArgumentException.class,
                () -> ticketService.addComment(ticket.getId(), "   "));
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void statusTransitionsFollowAllowedFlow() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Status test", "Desc", Category.TECHNICAL, Priority.MEDIUM, customer));

        // OPEN -> IN_PROGRESS
        Ticket updated = ticketService.changeStatus(ticket.getId(), Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, updated.getStatus());

        // IN_PROGRESS -> RESOLVED
        updated = ticketService.changeStatus(ticket.getId(), Status.RESOLVED);
        assertEquals(Status.RESOLVED, updated.getStatus());

        // RESOLVED -> CLOSED
        updated = ticketService.changeStatus(ticket.getId(), Status.CLOSED);
        assertEquals(Status.CLOSED, updated.getStatus());
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void invalidStatusTransitionIsRejected() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Invalid transition", "Desc", Category.GENERAL, Priority.LOW, customer));

        // OPEN -> RESOLVED is not valid
        assertThrows(IllegalStateException.class,
                () -> ticketService.changeStatus(ticket.getId(), Status.RESOLVED));

        // OPEN -> CLOSED is not valid
        assertThrows(IllegalStateException.class,
                () -> ticketService.changeStatus(ticket.getId(), Status.CLOSED));
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void inProgressAutoAssignsUnassignedTicket() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Auto-assign test", "Desc", Category.ACCESS, Priority.HIGH, customer));
        assertNull(ticket.getAssignedTo());

        Ticket updated = ticketService.changeStatus(ticket.getId(), Status.IN_PROGRESS);
        assertNotNull(updated.getAssignedTo());
        assertEquals("agent@test.com", updated.getAssignedTo().getEmail());
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void closedTicketDoesNotAllowStatusChanges() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Closed test", "Desc", Category.BILLING, Priority.LOW, customer));
        ticketService.changeStatus(ticket.getId(), Status.IN_PROGRESS);
        ticketService.changeStatus(ticket.getId(), Status.RESOLVED);
        ticketService.changeStatus(ticket.getId(), Status.CLOSED);

        Set<Status> validStatuses = ticketService.getValidNextStatuses(Status.CLOSED);
        assertTrue(validStatuses.isEmpty());
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void closedTicketDoesNotAllowNewComments() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Closed comment test", "Desc", Category.GENERAL, Priority.LOW, customer));
        ticketService.changeStatus(ticket.getId(), Status.IN_PROGRESS);
        ticketService.changeStatus(ticket.getId(), Status.RESOLVED);
        ticketService.changeStatus(ticket.getId(), Status.CLOSED);

        assertThrows(IllegalStateException.class,
                () -> ticketService.addComment(ticket.getId(), "Should fail"));
    }

    @Test
    @WithMockUser(username = "agent@test.com", roles = "ADMIN")
    void resolvedTicketCanBeReopened() {
        Ticket ticket = ticketRepository.save(
                new Ticket("Reopen test", "Desc", Category.GENERAL, Priority.MEDIUM, customer));
        ticketService.changeStatus(ticket.getId(), Status.IN_PROGRESS);
        ticketService.changeStatus(ticket.getId(), Status.RESOLVED);

        Ticket reopened = ticketService.changeStatus(ticket.getId(), Status.OPEN);
        assertEquals(Status.OPEN, reopened.getStatus());
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotAccessTicketDetail() {
        assertThrows(Exception.class, () -> navigate(TicketDetailView.class));
    }
}
