package com.example.specdriven.poster;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/posters")
public class PosterController {

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getPoster(
            @PathVariable String fileName,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        Path path = Path.of("posters", fileName);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        try {
            String etag = generateETag(path);

            if (etag.equals(ifNoneMatch)) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .eTag(etag)
                        .cacheControl(CacheControl.noCache())
                        .build();
            }

            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "image/png";
            }

            return ResponseEntity.ok()
                    .eTag(etag)
                    .cacheControl(CacheControl.noCache())
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new FileSystemResource(path));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String generateETag(Path path) throws IOException {
        long lastModified = Files.getLastModifiedTime(path).toMillis();
        long size = Files.size(path);
        return "\"" + Long.toHexString(lastModified) + "-" + Long.toHexString(size) + "\"";
    }
}
