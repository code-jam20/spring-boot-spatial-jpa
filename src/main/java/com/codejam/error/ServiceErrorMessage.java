package com.codejam.error;

import lombok.Getter;

public class ServiceErrorMessage {
    private final int code;
    private final int statusCode;
    private final String message;

    public ServiceErrorMessage(int code, int statusCode, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    public ServiceErrorMessage(final ServiceException exception) {
        this.code = exception.getServiceErrorsEnum().getCode();
        this.statusCode = exception.getServiceErrorsEnum().getStatusCode();
        this.message = exception.getServiceErrorsEnum().getMessage();
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
        return "ServiceErrorMessage{" +
                "code=" + code +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
