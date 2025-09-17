package com.example.officeManagementSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PerformanceScoreException extends RuntimeException {
    public PerformanceScoreException(String message) {
        super(message);
    }
}
