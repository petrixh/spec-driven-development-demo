package com.example.specdriven.ticket;

import com.example.specdriven.domain.Comment;

import java.time.LocalDateTime;

public record CommentInfo(
        Long id,
        String text,
        String authorName,
        LocalDateTime createdDate
) {
    public static CommentInfo fromComment(Comment comment) {
        return new CommentInfo(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreatedDate()
        );
    }
}
