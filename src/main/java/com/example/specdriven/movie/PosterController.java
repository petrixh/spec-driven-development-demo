package com.example.specdriven.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/posters")
public class PosterController {

    private static final Path POSTERS_DIR = Path.of("posters");

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getPoster(@PathVariable String filename, WebRequest request) throws IOException {
        Path file = POSTERS_DIR.resolve(filename).normalize();
        if (!file.startsWith(POSTERS_DIR) || !Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }
        long lastModified = Files.getLastModifiedTime(file).toMillis();
        long size = Files.size(file);
        String etag = "\"" + size + "-" + lastModified + "\"";
        if (request.checkNotModified(etag, lastModified)) {
            return null;
        }
        MediaType mediaType = filename.endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .eTag(etag)
                .lastModified(lastModified)
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }
}
