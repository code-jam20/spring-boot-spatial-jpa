package com.codejam.dao;

import com.codejam.dao.entities.Customer;
import com.codejam.dao.projections.CustomerNearBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides handles to database, to perform CRUD operations on the Customer table `CUSTOMER`.
 * The table is represented by the JPA entity {@link Customer}.
 * <p>
 * The interface also executes native queries and uses projection to build the location search DTO.
 *
 * @author Sohan
 * @see JpaRepository
 * @see Query
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * This will list all customers with their first name, last name and distance .
     *
     * @return List<CustomerNearBy>
     */
    @Query(value = "SELECT first_name AS firstName, last_name AS lastName, Round(ST_Distance(location, ST_GeomFromText(:spatialPointWkt, '4326')) / :divisor, 1) " +
            "as distance, :format AS distanceFormat FROM customer having distance < :maxDistance ORDER BY distance LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<CustomerNearBy> getCustomersNearBy(@Param("spatialPointWkt") final String spatialPointWkt,
                                            @Param("divisor") final Float divisor,
                                            @Param("format") final String format,
                                            @Param("maxDistance") final Double maxDistance,
                                            @Param("pageSize") final Integer pageSize,
                                            @Param("offset") final Integer offset);

    /**
     * This will list all customers based on first name.
     *
     * @return List<Customer>
     */
    @Query(name = "SELECT * FROM customer WHERE first_name = :firstName", nativeQuery = true)
    List<Customer> searchCustomerByFirstName(@Param("firstName") String firstName);
}
