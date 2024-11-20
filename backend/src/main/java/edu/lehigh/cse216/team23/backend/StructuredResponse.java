package edu.lehigh.cse216.team23.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * StructuredResponse provides a standard format for the server to respond to
 * clients.
 */
public class StructuredResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;

    public StructuredResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}