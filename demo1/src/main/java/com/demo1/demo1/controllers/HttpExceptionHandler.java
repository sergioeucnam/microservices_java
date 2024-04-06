package com.demo1.demo1.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class HttpExceptionHandler {
    /**
     * Exception handler for NoHandlerFoundException.
     * 
     * @param ex The NoHandlerFoundException object.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Route not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Exception handler for HttpRequestMethodNotSupportedException.
     * 
     * @param ex The HttpRequestMethodNotSupportedException object.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Method not supported");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
}