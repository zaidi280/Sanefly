package com.backend.sanfely.common.exception;

import com.backend.sanfely.common.dto.ApiErrorResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
            HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidOrderTransitionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTransition(InvalidOrderTransitionException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
            HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(ReviewNotAllowedException.class)
    public ResponseEntity<ApiErrorResponse> handleReviewNotAllowed(ReviewNotAllowedException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
            HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedActionException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
            HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        ApiErrorResponse body = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(), "Validation Failed", "One or more fields are invalid", fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
    	log.error("Unexpected error occurred", ex);
        ApiErrorResponse body = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Something went wrong"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorizationDenied(
            org.springframework.security.authorization.AuthorizationDeniedException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
            HttpStatus.FORBIDDEN.value(), "Forbidden", "You do not have permission to perform this action"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
}