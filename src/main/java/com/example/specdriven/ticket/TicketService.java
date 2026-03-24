package com.example.specdriven.ticket;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Comment;
import com.example.specdriven.domain.CommentRepository;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.domain.TicketRepository;
import com.example.specdriven.domain.User;
import com.example.specdriven.domain.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private static final Set<Status> VALID_TRANSITIONS_FROM_OPEN = Set.of(Status.IN_PROGRESS);
    private static final Set<Status> VALID_TRANSITIONS_FROM_IN_PROGRESS = Set.of(Status.RESOLVED, Status.OPEN);
    private static final Set<Status> VALID_TRANSITIONS_FROM_RESOLVED = Set.of(Status.CLOSED, Status.OPEN);

    public TicketService(TicketRepository ticketRepository, CommentRepository commentRepository,
            UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found: " + email));
    }

    @Transactional
    public Ticket submitTicket(String title, String description, Category category, Priority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        User user = getCurrentUser();
        Ticket ticket = new Ticket(title.trim(), description.trim(), category,
                priority != null ? priority : Priority.MEDIUM, user);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getMyTickets(Status statusFilter) {
        User user = getCurrentUser();
        if (statusFilter != null) {
            return ticketRepository.findByCreatedByAndStatusOrderByUpdatedDateDesc(user, statusFilter);
        }
        return ticketRepository.findByCreatedByOrderByUpdatedDateDesc(user);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + id));
    }

    public List<Comment> getComments(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        return commentRepository.findByTicketOrderByCreatedDateAsc(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByStatuses(List<Status> statuses) {
        return ticketRepository.findByStatusIn(statuses);
    }

    @Transactional
    public Ticket assignToMe(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        User agent = getCurrentUser();
        ticket.setAssignedTo(agent);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Comment addComment(Long ticketId, String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        Ticket ticket = getTicketById(ticketId);
        if (ticket.getStatus() == Status.CLOSED) {
            throw new IllegalStateException("Cannot add comments to a closed ticket");
        }
        User author = getCurrentUser();
        Comment comment = new Comment(text.trim(), ticket, author);
        ticket.setUpdatedDate(java.time.LocalDateTime.now());
        ticketRepository.save(ticket);
        return commentRepository.save(comment);
    }

    @Transactional
    public Ticket changeStatus(Long ticketId, Status newStatus) {
        Ticket ticket = getTicketById(ticketId);
        Status currentStatus = ticket.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalStateException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        ticket.setStatus(newStatus);

        // Auto-assign when moving to IN_PROGRESS if unassigned
        if (newStatus == Status.IN_PROGRESS && ticket.getAssignedTo() == null) {
            ticket.setAssignedTo(getCurrentUser());
        }

        return ticketRepository.save(ticket);
    }

    public Set<Status> getValidNextStatuses(Status currentStatus) {
        return switch (currentStatus) {
            case OPEN -> VALID_TRANSITIONS_FROM_OPEN;
            case IN_PROGRESS -> VALID_TRANSITIONS_FROM_IN_PROGRESS;
            case RESOLVED -> VALID_TRANSITIONS_FROM_RESOLVED;
            case CLOSED -> Set.of();
        };
    }

    private boolean isValidTransition(Status from, Status to) {
        return getValidNextStatuses(from).contains(to);
    }

    // Dashboard metrics
    public long countByStatus(Status status) {
        return ticketRepository.countByStatus(status);
    }

    public long countResolvedToday() {
        return ticketRepository.countByStatusAndUpdatedDateAfter(
                Status.RESOLVED, LocalDate.now().atStartOfDay());
    }

    public long countClosedToday() {
        return ticketRepository.countByStatusAndUpdatedDateAfter(
                Status.CLOSED, LocalDate.now().atStartOfDay());
    }

    public long countNonClosedByCategory(Category category) {
        return ticketRepository.countByStatusInAndCategory(
                List.of(Status.OPEN, Status.IN_PROGRESS, Status.RESOLVED), category);
    }

    public long countNonClosedByPriority(Priority priority) {
        return ticketRepository.countByStatusInAndPriority(
                List.of(Status.OPEN, Status.IN_PROGRESS, Status.RESOLVED), priority);
    }
}
