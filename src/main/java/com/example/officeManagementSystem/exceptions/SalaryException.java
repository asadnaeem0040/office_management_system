package com.example.officeManagementSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SalaryException extends RuntimeException {
    public SalaryException(String message) {
        super(message);
    }
}
