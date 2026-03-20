package com.example.specdriven.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/posters")
public class PosterController {

    private final Path postersDirectory = Path.of("posters").toAbsolutePath().normalize();

    @GetMapping("/{fileName:.+}")
    ResponseEntity<Resource> getPoster(@PathVariable String fileName, WebRequest request) throws IOException {
        Path posterPath = postersDirectory.resolve(fileName).normalize();
        if (!posterPath.startsWith(postersDirectory) || !Files.isRegularFile(posterPath)) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        String eTag = posterEtag(posterPath);
        if (request.checkNotModified(eTag)) {
            return ResponseEntity.status(304)
                    .cacheControl(CacheControl.noCache())
                    .eTag(eTag)
                    .build();
        }

        Resource resource = new UrlResource(posterPath.toUri());
        MediaType contentType = MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .eTag(eTag)
                .contentType(contentType)
                .body(resource);
    }

    private String posterEtag(Path posterPath) throws IOException {
        return Files.getLastModifiedTime(posterPath).toMillis() + "-" + Files.size(posterPath);
    }
}
