package com.example.specdriven.ticket;

import com.example.specdriven.data.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;

    public TicketService(TicketRepository ticketRepository, CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
    }

    public Ticket createTicket(String title, String description, Category category, Priority priority, User createdBy) {
        return ticketRepository.save(new Ticket(title, description, category, priority, createdBy));
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByCreatedByOrderByUpdatedDateDesc(user);
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllByOrderByUpdatedDateDesc();
    }

    public List<Ticket> getNonClosedTickets() {
        return ticketRepository.findByStatusInOrderByUpdatedDateDesc(
                List.of(Status.OPEN, Status.IN_PROGRESS, Status.RESOLVED));
    }

    public Ticket assignTicket(Long ticketId, User agent) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setAssignedTo(agent);
        return ticketRepository.save(ticket);
    }

    public Ticket updateStatus(Long ticketId, Status newStatus, User agent) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        validateStatusTransition(ticket.getStatus(), newStatus);
        ticket.setStatus(newStatus);
        if (newStatus == Status.IN_PROGRESS && ticket.getAssignedTo() == null) {
            ticket.setAssignedTo(agent);
        }
        return ticketRepository.save(ticket);
    }

    public Comment addComment(Long ticketId, String text, User author) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        if (ticket.getStatus() == Status.CLOSED) {
            throw new IllegalStateException("Cannot add comments to closed tickets");
        }
        Comment comment = new Comment(text, author, ticket);
        ticket.setUpdatedDate(LocalDateTime.now());
        ticketRepository.save(ticket);
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        return commentRepository.findByTicketOrderByCreatedDateAsc(ticket);
    }

    public List<Status> getValidNextStatuses(Status current) {
        return switch (current) {
            case OPEN -> List.of(Status.IN_PROGRESS);
            case IN_PROGRESS -> List.of(Status.RESOLVED, Status.OPEN);
            case RESOLVED -> List.of(Status.CLOSED, Status.OPEN);
            case CLOSED -> List.of();
        };
    }

    public long countByStatus(Status status) {
        return ticketRepository.countByStatus(status);
    }

    public long countResolvedToday() {
        return ticketRepository.countByStatusAndUpdatedDateAfter(Status.RESOLVED, LocalDate.now().atStartOfDay());
    }

    public long countClosedToday() {
        return ticketRepository.countByStatusAndUpdatedDateAfter(Status.CLOSED, LocalDate.now().atStartOfDay());
    }

    public Map<Category, Long> countNonClosedByCategory() {
        return getNonClosedTickets().stream()
                .collect(Collectors.groupingBy(Ticket::getCategory, Collectors.counting()));
    }

    public Map<Priority, Long> countNonClosedByPriority() {
        return getNonClosedTickets().stream()
                .collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));
    }

    private void validateStatusTransition(Status current, Status target) {
        if (!getValidNextStatuses(current).contains(target)) {
            throw new IllegalStateException("Cannot transition from " + current + " to " + target);
        }
    }
}
