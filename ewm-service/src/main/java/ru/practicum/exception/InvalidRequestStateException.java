package ru.practicum.exception;

import ru.practicum.event.enums.Status;

public class InvalidRequestStateException extends RuntimeException {
    public InvalidRequestStateException(String s,Status status) {
            super();
        }
}
