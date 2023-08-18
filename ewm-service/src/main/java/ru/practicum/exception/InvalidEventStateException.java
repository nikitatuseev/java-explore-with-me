package ru.practicum.exception;

import ru.practicum.event.enums.State;

public class InvalidEventStateException extends RuntimeException {

    private static final String INVALID_EVENT_STATE_PENDING_MESSAGE = "Событие должно иметь статус PENDING. Текущее состояние - %s.";
    private static final String INVALID_EVENT_STATE_MESSAGE = "Событие должно иметь статус %s. Текущее состояние - %s.";

    public InvalidEventStateException(State state) {
        super(String.format(INVALID_EVENT_STATE_PENDING_MESSAGE, state.toString()));
    }

    public InvalidEventStateException(State expectedState, State actualState) {
        super(String.format(INVALID_EVENT_STATE_MESSAGE, expectedState.toString(), actualState.toString()));
    }
}
