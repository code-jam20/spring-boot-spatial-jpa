package com.codejam.service.impl;

import com.codejam.dao.CustomerRepository;
import com.codejam.dao.entities.Customer;
import com.codejam.dao.projections.CustomerNearBy;
import com.codejam.error.ServiceErrorsEnum;
import com.codejam.error.ServiceException;
import com.codejam.service.CustomerService;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.codejam.utils.ValidationUtils.validateFormat;
import static com.codejam.utils.ValidationUtils.validateLatLng;

/**
 * Service Implementation that fetch customers / perform geo spatial search.
 *
 * @author tdhanjal
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private static final Float METER_TO_MILES_DIVISOR = 1609.344F;
    private static final Float METER_TO_KILOMETERS_DIVISOR = 1000F;

    /**
     * Handle to the Data Access Layer.
     */
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Create customer with name and location.
     *
     * @param firstName
     * @param lastName
     * @param latitude
     * @param longitude
     * @return CustomerNearBy
     */
    @Override
    public CompletionStage<Customer> createCustomer(String firstName, String lastName, Double latitude, Double longitude) {
        LOGGER.info("Creating customer");
        try {
            // Convert latitude / longitude to well known text format https://en.wikipedia.org/wiki/Well-known_text_representation_of_geometry
            final String wktLocationText = "POINT(" + longitude.toString() + " " + latitude.toString() + ")";
            final Point point = (Point) wktToGeometry(wktLocationText);

            // Set SRID to apply geographic coordinates on Point
            point.setSRID(4326);

            // Persist Customer
            final Customer customerLocation = customerRepository.save(new Customer(firstName, lastName, point));
            return CompletableFuture.completedFuture(customerLocation);
        } catch (Exception ex) {
            if (ex instanceof DataIntegrityViolationException || ex instanceof ConstraintViolationException) {
                LOGGER.warn("MySQL exception " + ex);
                throw new ServiceException(ServiceErrorsEnum.CUSTOMER_ALREADY_EXIST);
            } else {
                LOGGER.error("Unable to process request because of " + ex);
                throw new ServiceException(ServiceErrorsEnum.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Gets near by customers with distance.
     *
     * @param latitude
     * @param longitude
     * @return CustomerNearBy
     */
    @Override
    public CompletionStage<List<CustomerNearBy>> getCustomersNearBy(Double latitude, Double longitude, String format, Double maxDistance, Integer pageSize, Integer pageNum) {
        validateFormat(format);
        validateLatLng(latitude, longitude);

        final String pointStr = "POINT(" + latitude.toString() + " " + longitude.toString() + ")";
        final Float divisor = (format.equalsIgnoreCase("mi")) ? METER_TO_MILES_DIVISOR : METER_TO_KILOMETERS_DIVISOR;

        List<CustomerNearBy> customers = null;
        try {
            final Integer offset = (pageSize > 1) ? pageSize * (pageNum - 1) : 0;
            customers = customerRepository.getCustomersNearBy(pointStr, divisor, format.toLowerCase(), maxDistance, pageSize, offset);
        } catch (Exception e) {
            LOGGER.info("Failed to get near by customer because " + e);
        }

        // Throw valid error when no customers found
        if (customers.isEmpty()) {
            throw new ServiceException(ServiceErrorsEnum.CUSTOMER_NOT_FOUND);
        }
        return CompletableFuture.completedFuture(customers);
    }

    /**
     * Search for customer based on first name.
     *
     * @param firstName
     * @return List<Customer>
     */
    @Override
    public CompletionStage<List<Customer>> searchCustomerByFirstName(String firstName) {
        final List<Customer> customers = customerRepository.searchCustomerByFirstName(firstName);

        // Throw valid error when no customers found
        if (customers.isEmpty()) {
            throw new ServiceException(ServiceErrorsEnum.CUSTOMER_NOT_FOUND);
        }
        return CompletableFuture.completedFuture(customers);
    }

    private Geometry wktToGeometry(String wellKnownText)
            throws ParseException {
        return new WKTReader().read(wellKnownText);
    }
}
