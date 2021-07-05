package com.codejam.controller;

import com.codejam.service.impl.CustomerServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import java.util.Optional;

import static com.codejam.utils.AsyncUtil.completeResponse;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * This jersey controller handles all the API calls made on the end point.
 * <p>
 * - /customers: Gets the customers by matching first name
 * - /customers/nearby: Gets the customers with distance from queried location
 *
 * @author tdhanjal
 */
@Path("/secure")
@Produces(APPLICATION_JSON)
public class CustomerResource {

    @Autowired
    CustomerServiceImpl customerService;

    @GET
    @Path("/health")
    public void getHealthCheck(@Suspended AsyncResponse asyncResponse) {
        asyncResponse.resume(ImmutableMap.of("status", "ok"));
    }

    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void createCustomer(@FormParam("FirstName") final String firstName,
                               @FormParam("LastName") final String lastName,
                               @FormParam("Latitude") final Double latitude,
                               @FormParam("Longitude") final Double longitude,
                               @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        customerService.createCustomer(firstName,
                                lastName, latitude, longitude),
                asyncResponse);
    }

    @GET
    @Path("/customers")
    public void getCustomer(@QueryParam("FirstName") final String firstName,
                            @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        customerService.searchCustomerByFirstName(firstName),
                asyncResponse);
    }

    @GET
    @Path("/customers/nearby")
    public void getCustomersNearBy(@QueryParam("Latitude") final Double latitude,
                                   @QueryParam("Longitude") final Double longitude,
                                   @DefaultValue("MI") @QueryParam("Format") final String format,
                                   @DefaultValue("100") @QueryParam("MaxDistance") final Double maxDistance,
                                   @DefaultValue("10") @QueryParam("PageSize") final Integer pageSize,
                                   @DefaultValue("1") @QueryParam("PageNum") final Integer pageNum,
                                   @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        customerService.getCustomersNearBy(latitude, longitude, format, maxDistance, pageSize, pageNum),
                asyncResponse);
    }
}
