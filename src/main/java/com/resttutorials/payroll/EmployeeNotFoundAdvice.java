package com.resttutorials.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeNotFoundAdvice {
    
    @ResponseBody
    // Signals that this advice is rendered straight to the response body
    @ExceptionHandler(EmployeeNotFoundException.class)
    // configures the advice to only respond if an EmployeeNotFoundException is thrown
    @ResponseStatus(HttpStatus.NOT_FOUND)
    // says to issue an HTTP 404 response
    String employeeNotFoundHandler(EmployeeNotFoundException ex) {
        // The body of the method generates the content -> give the message of the exception
        return ex.getMessage();
    }
}
