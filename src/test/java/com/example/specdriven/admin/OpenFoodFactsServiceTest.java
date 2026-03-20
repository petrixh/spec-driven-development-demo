package com.example.specdriven.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OpenFoodFactsServiceTest {

    private HttpClient mockHttpClient;
    private OpenFoodFactsService service;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        mockHttpClient = mock(HttpClient.class);
        service = new OpenFoodFactsService(mockHttpClient);
    }

    @SuppressWarnings("unchecked")
    @Test
    void successfulLookupReturnsProductName() throws Exception {
        String json = """
                {
                  "status": 1,
                  "product": {
                    "product_name": "Nutella"
                  }
                }
                """;

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(json);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Optional<String> result = service.lookupProductName("3017620422003");
        assertTrue(result.isPresent());
        assertEquals("Nutella", result.get());
    }

    @SuppressWarnings("unchecked")
    @Test
    void notFoundReturnsEmpty() throws Exception {
        String json = """
                {
                  "status": 0
                }
                """;

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(json);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Optional<String> result = service.lookupProductName("0000000000000");
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void apiFailureReturnsEmpty() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Connection timeout"));

        Optional<String> result = service.lookupProductName("1234567890");
        assertTrue(result.isEmpty());
    }
}
