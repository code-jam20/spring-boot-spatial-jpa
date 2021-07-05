package com.codejam;

import com.google.common.net.MediaType;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import java.util.Optional;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseTest {
    static final String ACCEPT_MEDIA_TYPE = MediaType.JSON_UTF_8.toString();
    static final String BASE_URL = "http://localhost:8080/spatial";
    static final int REQUEST_TIMEOUT_IN_MS = 10000;
    static final int CONNECTION_TIMEOUT_IN_MS = 10000;

    static final MySQLContainer DATABASE = new MySQLContainer("mysql:latest");
    static AsyncHttpClient httpClient;

    static {
        DATABASE.start();
        httpClient = asyncHttpClient(config().setRequestTimeout(REQUEST_TIMEOUT_IN_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_IN_MS));
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DATABASE::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE::getUsername);
        registry.add("spring.datasource.password", DATABASE::getPassword);
    }

    public BoundRequestBuilder createPost(final String url, Optional<MediaType> bodyType) {
        final BoundRequestBuilder boundRequestBuilder = httpClient.preparePost(url);
        boundRequestBuilder.addHeader("Accept", ACCEPT_MEDIA_TYPE);
        if(bodyType.isPresent()) {
            boundRequestBuilder.addHeader("Content-Type", bodyType.get().toString());
        }
        return boundRequestBuilder;
    }

    public BoundRequestBuilder createGet(final String url) {
        final BoundRequestBuilder boundRequestBuilder = httpClient.prepareGet(url);
        boundRequestBuilder.addHeader("Accept", ACCEPT_MEDIA_TYPE);
        return boundRequestBuilder;
    }
}