package org.example.backendlibrary.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.example.backendlibrary.dtos.responses.ErrorResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    ResponseEntity<Response<Void>> handleException(Exception ex) {
        log.error("Exception: {}", ex.toString());

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        Response<Void> response =
                Response.<Void>builder().success(false).error(errorResponse).build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<Response<Void>> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        Response<Void> response =
                Response.<Void>builder().success(false).error(errorResponse).build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }
}
