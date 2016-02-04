package com.solution.backend.responses;

/**
 *
 * @author dragomir
 * @param <T>
 */
public class BackendResponse<T> {

    private final int status;
    private final String description;
    private final T data;

    public BackendResponse(int status, String message, T data) {
        this.status = status;
        this.description = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public T getData() {
        return data;
    }
}
