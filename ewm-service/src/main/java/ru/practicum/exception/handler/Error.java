package ru.practicum.exception.handler;

import lombok.Getter;

@Getter
public class Error {
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;

    public Error(String status, String reason, String message, String timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }
}