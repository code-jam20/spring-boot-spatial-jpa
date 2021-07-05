package com.codejam.utils;

import com.codejam.error.ServiceErrorsEnum;
import com.codejam.error.ServiceException;

public class ValidationUtils {
    public static void validateFormat(final String format) {
        if(!format.equalsIgnoreCase("mi") && !format.equalsIgnoreCase("km")) {
            throw new ServiceException(ServiceErrorsEnum.BAD_REQUEST, "Not a valid distance format");
        }
    }

    public static void validateLatLng(final Double lat, final Double lng) {
        if(lat == null || lng == null) {
            throw new ServiceException(ServiceErrorsEnum.BAD_REQUEST, "Need Latitude and Longitude in Query param");
        }
    }
}
