package com.codejam.dao.projections;

/**
 * Projection use perform lat lng near by query on customers
 */
public interface CustomerNearBy {
    String getFirstName();
    String getLastName();
    Float getDistance();
    String getDistanceFormat();
}
