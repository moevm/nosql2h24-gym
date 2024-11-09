package com.example.gym.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.gym.exception.AuthenticationException;
import com.example.gym.exception.InvalidDataException;
import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.dto.ResponseError;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseError> handleResourceNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseError(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    @ExceptionHandler(UniquenessViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleUniquenessViolationException(UniquenessViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseError(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleInvalidDataException(InvalidDataException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseError(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseError> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ResponseError(HttpStatus.FORBIDDEN.value(), exception.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseError> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseError(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ResponseEntity<ResponseError> handleException(Exception exception) {
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
    //             new ResponseError(
    //                     HttpStatus.INTERNAL_SERVER_ERROR.value(), 
    //                     "Ошибка сервера: " + exception.getMessage()
    //     ));
    // }

}
