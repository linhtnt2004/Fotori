package com.example.fotori.exception;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.CustomException;
import com.example.fotori.common.enums.ErrorCode;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handlerCustomException(CustomException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse(
                e.getErrorCode().name(),
                e.getMessage(),
                null
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlerMethodArgumentNotValid(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .findFirst()
            .orElse("Invalid request");

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse(
                ErrorCode.BAD_REQUEST.name(),
                message,
                null
            ));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handlerBusinessException(BusinessException e) {
        // Return FORBIDDEN with business code in the message field so the frontend can act on it
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ApiResponse(
                ErrorCode.FORBIDDEN.name(),
                e.getCode(),
                null
            ));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse> handlerDataAccess(DataAccessException e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                "Database error occurred!",
                null
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlerGenericException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                e.getMessage() != null ? e.getMessage() : "An unexpected error occurred",
                null
            ));
    }
}
