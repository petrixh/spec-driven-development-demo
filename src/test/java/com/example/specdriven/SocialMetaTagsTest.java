package com.example.specdriven;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SocialMetaTagsTest {

    private static final Duration FRONTEND_BUILD_TIMEOUT = Duration.ofSeconds(90);

    @LocalServerPort
    private int port;

    @Test
    void rootPageIncludesOpenGraphAndTwitterMetaTagsInServerRenderedHtml() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = fetchRenderedHtml(client, "/");

        assertThat(response.body()).contains("property=\"og:title\" content=\"CineMax\"");
        assertThat(response.body()).contains("property=\"og:description\" content=\"" + Application.SITE_DESCRIPTION + "\"");
        assertThat(response.body()).contains("property=\"og:image\" content=\"http://127.0.0.1:" + port + Application.OG_IMAGE_PATH + "\"");
        assertThat(response.body()).contains("property=\"og:url\" content=\"http://127.0.0.1:" + port + "/\"");
        assertThat(response.body()).contains("property=\"og:type\" content=\"website\"");
        assertThat(response.body()).contains("name=\"twitter:card\" content=\"summary_large_image\"");
        assertThat(response.body()).contains("name=\"twitter:title\" content=\"CineMax\"");
        assertThat(response.body()).contains("name=\"twitter:description\" content=\"" + Application.SITE_DESCRIPTION + "\"");
        assertThat(response.body()).contains("name=\"twitter:image\" content=\"http://127.0.0.1:" + port + Application.OG_IMAGE_PATH + "\"");
    }

    @Test
    void nestedRouteIncludesOpenGraphAndTwitterMetaTagsInServerRenderedHtml() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = fetchRenderedHtml(client, "/movie/1");

        assertThat(response.body()).contains("property=\"og:title\" content=\"CineMax\"");
        assertThat(response.body()).contains("property=\"og:url\" content=\"http://127.0.0.1:" + port + "/movie/1\"");
        assertThat(response.body()).contains("name=\"twitter:image\" content=\"http://127.0.0.1:" + port + Application.OG_IMAGE_PATH + "\"");
    }

    @Test
    void ogImageIsServedAsStaticAssetWithSocialPreviewDimensions() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + port + Application.OG_IMAGE_PATH))
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("content-type")).hasValue("image/png");

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(response.body()));
        assertThat(image).isNotNull();
        assertThat(image.getWidth()).isGreaterThanOrEqualTo(1200);
        assertThat(image.getHeight()).isGreaterThanOrEqualTo(630);
    }

    private HttpResponse<String> fetchRenderedHtml(HttpClient client, String path) throws Exception {
        long deadline = System.nanoTime() + FRONTEND_BUILD_TIMEOUT.toNanos();
        HttpResponse<String> latestResponse = null;

        while (System.nanoTime() < deadline) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:" + port + path))
                    .GET()
                    .build();

            latestResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!latestResponse.body().contains("Building front-end development bundle")
                    && latestResponse.body().contains("property=\"og:title\"")) {
                return latestResponse;
            }

            Thread.sleep(1000);
        }

        assertThat(latestResponse).isNotNull();
        throw new AssertionError("Timed out waiting for server-rendered HTML with social meta tags. Last response body:\n"
                + latestResponse.body());
    }
}
