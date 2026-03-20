package com.example.specdriven.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenFoodFactsService {

    private static final Logger log = LoggerFactory.getLogger(OpenFoodFactsService.class);
    private static final String API_URL = "https://world.openfoodfacts.org/api/v2/product/";
    private static final Pattern PRODUCT_NAME_PATTERN =
            Pattern.compile("\"product_name\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern STATUS_PATTERN =
            Pattern.compile("\"status\"\\s*:\\s*(\\d+)");

    private final HttpClient httpClient;

    public OpenFoodFactsService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    // Package-private constructor for testing
    OpenFoodFactsService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<String> lookupProductName(String barcode) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + barcode))
                    .timeout(Duration.ofSeconds(5))
                    .header("User-Agent", "BuyBuyPOS/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            String body = response.body();

            // Check status field
            Matcher statusMatcher = STATUS_PATTERN.matcher(body);
            if (!statusMatcher.find() || !"1".equals(statusMatcher.group(1))) {
                return Optional.empty();
            }

            // Extract product_name
            Matcher nameMatcher = PRODUCT_NAME_PATTERN.matcher(body);
            if (!nameMatcher.find()) {
                return Optional.empty();
            }

            String productName = nameMatcher.group(1).trim();
            if (productName.isBlank()) {
                return Optional.empty();
            }

            return Optional.of(productName);
        } catch (Exception e) {
            log.debug("Open Food Facts lookup failed for barcode {}: {}", barcode, e.getMessage());
            return Optional.empty();
        }
    }
}
