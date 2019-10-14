package com.bogdan.persistentweb.dto;

public class ErrorDetails {
    private String field;
    private String message;

    public ErrorDetails(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
