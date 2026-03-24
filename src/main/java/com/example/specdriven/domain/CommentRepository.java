package com.example.specdriven.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTicketOrderByCreatedDateAsc(Ticket ticket);
}
