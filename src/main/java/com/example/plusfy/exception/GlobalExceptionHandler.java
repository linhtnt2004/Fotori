package com.example.plusfy.exception;

import com.example.plusfy.common.ApiResponse;
import com.example.plusfy.common.CustomException;
import com.example.plusfy.common.ErrorCode;
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
        .getAllErrors()
        .get(0)
        .getDefaultMessage();
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse(
        ErrorCode.INTERNAL_SERVER_ERROR.name(),
        message,
        null
        ));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse> handlerDataAccess(DataAccessException e) {
        return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse(
        ErrorCode.INTERNAL_SERVER_ERROR.name(),
        "Unexcepted error orrcured!",
        null
        ));
    }
}
