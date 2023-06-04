package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleAlreadyExistException(AlreadyExistException alreadyExistException) {
        return new ExceptionResponse(alreadyExistException.getMessage());
    }

    @ExceptionHandler(IncorrectInputException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleInputException(IncorrectInputException incorrectInputException) {
        return new ExceptionResponse(incorrectInputException.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException notFoundException) {
        return new ExceptionResponse(notFoundException.getMessage());
    }

    @ExceptionHandler(IncorrectDateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleIncorrectDateException(IncorrectDateException incorrectDateException) {
        return new ExceptionResponse(incorrectDateException.getMessage());
    }

    @ExceptionHandler(OverflowException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleOverflowException(OverflowException overflowException) {
        return new ExceptionResponse(overflowException.getMessage());
    }
}
