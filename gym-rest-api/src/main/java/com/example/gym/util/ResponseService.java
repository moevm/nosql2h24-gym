package com.example.gym.util;

import com.example.gym.model.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public ResponseEntity<?> getNotFoundResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseError.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(message)
                        .build());
    }

    public ResponseEntity<?> getBadRequestResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ResponseError.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build());
    }
}
