package com.example.specdriven.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PosterControllerTest {

    @LocalServerPort
    private int port;

    @Test
    void posterResponsesUseNoCacheAndExposeEtag() throws Exception {
        HttpResponse<Void> response = send(HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + port + "/api/posters/ai-developer-2.png"))
                .GET()
                .build(), HttpResponse.BodyHandlers.discarding());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("cache-control")).hasValue("no-cache");
        assertThat(response.headers().firstValue("etag")).hasValueSatisfying(etag -> assertThat(etag).isNotBlank());
        assertThat(response.headers().firstValue("content-type")).hasValue("image/png");
    }

    @Test
    void posterResponsesReturnNotModifiedWhenEtagMatches() throws Exception {
        HttpResponse<Void> initialResponse = send(HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + port + "/api/posters/ai-developer-2.png"))
                .GET()
                .build(), HttpResponse.BodyHandlers.discarding());

        String etag = initialResponse.headers().firstValue("etag").orElseThrow();

        HttpResponse<Void> revalidatedResponse = send(HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + port + "/api/posters/ai-developer-2.png"))
                .header("If-None-Match", etag)
                .GET()
                .build(), HttpResponse.BodyHandlers.discarding());

        assertThat(revalidatedResponse.statusCode()).isEqualTo(304);
        assertThat(revalidatedResponse.headers().firstValue("cache-control")).hasValue("no-cache");
        assertThat(revalidatedResponse.headers().firstValue("etag")).hasValue(etag);
    }

    private <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws Exception {
        return HttpClient.newHttpClient().send(request, bodyHandler);
    }
}
