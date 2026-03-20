package com.example.specdriven.catalog.application;

public record PosterUpload(
        String originalFileName,
        byte[] content
) {
}
