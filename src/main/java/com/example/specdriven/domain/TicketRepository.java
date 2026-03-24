package com.example.specdriven.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreatedByOrderByUpdatedDateDesc(User createdBy);

    List<Ticket> findByCreatedByAndStatusOrderByUpdatedDateDesc(User createdBy, Status status);

    List<Ticket> findByStatusIn(List<Status> statuses);

    long countByStatus(Status status);

    long countByStatusAndUpdatedDateAfter(Status status, LocalDateTime after);

    long countByStatusInAndCategory(List<Status> statuses, Category category);

    long countByStatusInAndPriority(List<Status> statuses, Priority priority);
}
