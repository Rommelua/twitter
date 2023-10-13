package com.example.twitter.controller

import com.example.twitter.exception.EntityNotFoundException
import com.example.twitter.exception.RegistrationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import java.time.LocalDateTime
import java.util.stream.Collectors

@ControllerAdvice
class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>()
        body.put("timestamp", LocalDateTime.now())
        body.put("status", HttpStatus.NOT_FOUND)
        body.put("errors", "Entity not found")
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = RegistrationException.class)
    protected ResponseEntity<Object> handleRegistrationException(
            RegistrationException ex,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>()
        body.put("timestamp", LocalDateTime.now())
        body.put("status", HttpStatus.BAD_REQUEST)
        body.put("errors", ex.getMessage())
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>()
        body.put("timestamp", LocalDateTime.now())
        body.put("status", HttpStatus.BAD_REQUEST)
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(this::getErrorMessage)
                .collect(Collectors.toList())
        body.put("errors", errors)
        return new ResponseEntity<>(body, headers, status)
    }

    private String getErrorMessage(ObjectError e) {
        String message = e.getDefaultMessage();
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField()
            message = field + " " + message
        }
        return message
    }
}
