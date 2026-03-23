package com.example.specdriven.data;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedByOrderByUpdatedDateDesc(User createdBy);
    List<Ticket> findByStatusInOrderByUpdatedDateDesc(List<Status> statuses);
    List<Ticket> findAllByOrderByUpdatedDateDesc();
    long countByStatus(Status status);
    long countByStatusAndUpdatedDateAfter(Status status, LocalDateTime after);
}
