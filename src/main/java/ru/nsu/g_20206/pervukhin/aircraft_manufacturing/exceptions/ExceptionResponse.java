package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions;

public class ExceptionResponse {
    String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
