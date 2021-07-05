package com.codejam.service;

import com.codejam.dao.entities.Customer;
import com.codejam.dao.projections.CustomerNearBy;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Service interface that masks the caller from the implementation that fetches / acts on Customer
 * related data.
 *
 * @author tdhanjal
 */
public interface CustomerService {

    /**
     * Creates new customer.
     *
     * @param firstName
     * @param lastName
     * @param latitude
     * @param longitude
     * @return Customer
     */
    CompletionStage<Customer> createCustomer(String firstName, String lastName, Double latitude, Double longitude);

    /**
     * Gets near by customers with distance.
     *
     * @param latitude
     * @param longitude
     * @return List<CustomerNearBy>
     */
    CompletionStage<List<CustomerNearBy>> getCustomersNearBy(Double latitude, Double longitude, String format, Double maxDistance, Integer pageSize, Integer pageNum);

    /**
     * Search for customer based on first name.
     *
     * @param firstName
     * @return List<Customer>
     */
    CompletionStage<List<Customer>> searchCustomerByFirstName(String firstName);
}
