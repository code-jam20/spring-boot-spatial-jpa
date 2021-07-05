package com.codejam;

import com.google.common.net.MediaType;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Param;
import org.asynchttpclient.Response;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerResourceTest extends BaseTest {
    private final String CUSTOMER_RESOURCE = "/secure/customers";
    private final String FIRST_NAME_PARAM = "FirstName";
    private final String LAST_NAME_PARAM = "LastName";
    private final String LATITUDE_PARAM = "Latitude";
    private final String LONGITUDE_PARAM = "Longitude";
    private final String MAX_DISTANCE_PARAM = "MaxDistance";

    @Test
    public void testCreateCustomer() throws InterruptedException, ExecutionException, TimeoutException {
        final BoundRequestBuilder builder = createPost(BASE_URL + CUSTOMER_RESOURCE, Optional.of(MediaType.FORM_DATA))
                .setFormParams(asList(new Param(FIRST_NAME_PARAM, "Ajay"),
                        new Param(LAST_NAME_PARAM, "Jain"),
                        new Param(LATITUDE_PARAM, "37.482052"),
                        new Param(LONGITUDE_PARAM, "-122.239488")));

        final Response response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());
        final String responseBody = new String(response.getResponseBody().getBytes());
        assertThat(responseBody).contains("\"firstName\":\"Ajay\"");
    }

    @Test
    public void testGetCustomer() throws InterruptedException, ExecutionException, TimeoutException {
        BoundRequestBuilder builder = createPost(BASE_URL + CUSTOMER_RESOURCE, Optional.of(MediaType.FORM_DATA))
                .setFormParams(asList(new Param(FIRST_NAME_PARAM, "Sahil"),
                        new Param(LAST_NAME_PARAM, "Jain"),
                        new Param(LATITUDE_PARAM, "37.482052"),
                        new Param(LONGITUDE_PARAM, "-122.239488")));

        Response response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());

        builder = createGet(BASE_URL + CUSTOMER_RESOURCE)
                .setQueryParams(asList(new Param(FIRST_NAME_PARAM, "Sahil")));

        response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());
        final String responseBody = new String(response.getResponseBody().getBytes());
        assertThat(responseBody).contains("\"firstName\":\"Sahil\"");
    }

    @Test
    public void testGetCustomerNearBy() throws InterruptedException, ExecutionException, TimeoutException {
        final List<List<String>> inputs = asList(
                asList("John", "Doe", "37.98", "-122.05"), // Concord,CA
                asList("Jane", "Doe", "36.77", "-119.72"), // Fresno,CA
                asList("Brain", "Lara", "38.22", "-122.282"), // Napa,CA
                asList("Kyle", "Doe", "38.90", "-120.00")); // Lake Tahoe,CA

        for (List<String> input : inputs) {
            final BoundRequestBuilder builder = createPost(BASE_URL + CUSTOMER_RESOURCE, Optional.of(MediaType.FORM_DATA))
                    .setFormParams(asList(new Param(FIRST_NAME_PARAM, input.get(0)),
                            new Param(LAST_NAME_PARAM, input.get(1)),
                            new Param(LATITUDE_PARAM, input.get(2)),
                            new Param(LONGITUDE_PARAM, input.get(3))));

            final Response response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
            assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());
        }

        // Find places with 90 miles of Cupertino, CA
        final BoundRequestBuilder builder = createGet(BASE_URL + CUSTOMER_RESOURCE + "/nearby")
                .setQueryParams(asList(new Param(LATITUDE_PARAM, "37.3230"),
                        new Param(LONGITUDE_PARAM, "-122.0322"),
                        new Param(LONGITUDE_PARAM, "-122.0322"),
                        new Param(MAX_DISTANCE_PARAM, "90")));

        // Kyle should not be included
        final Response response = httpClient.executeRequest(builder.build()).get(10, TimeUnit.SECONDS);
        assertThat(response.getStatusCode()).isEqualTo(javax.ws.rs.core.Response.Status.OK.getStatusCode());
        final String responseBody = new String(response.getResponseBody().getBytes());
        assertThat(responseBody).doesNotContain("\"firstName\":\"Kyle\"");
    }
}
