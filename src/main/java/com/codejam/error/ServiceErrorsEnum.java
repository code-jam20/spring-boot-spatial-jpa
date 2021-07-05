package com.codejam.error;

public enum ServiceErrorsEnum {
    REQUEST_TIME_OUT(1000, 408, "Request Timed Out"),
    BAD_REQUEST(1001, 400, "Bad Request"),
    INTERNAL_SERVER_ERROR(1002, 500, "Internal Server Error"),
    CUSTOMER_NOT_FOUND(3001, 404, "No Customers Found"),
    CUSTOMER_ALREADY_EXIST(3002, 403, "Customer already exist");

    private int code;
    private int statusCode;
    public String message;

    ServiceErrorsEnum(final int code, final int statusCode, final String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ServiceError{" +
                "code=" + code +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
