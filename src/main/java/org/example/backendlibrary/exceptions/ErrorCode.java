package org.example.backendlibrary.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    PRODUCT_NOTFOUND(1001, "Document not found", HttpStatus.BAD_REQUEST),
    GENRE_NOTFOUND(1001, "Genre not found", HttpStatus.BAD_REQUEST),
    LIBRARY_NOTFOUND(1001, "Library not found", HttpStatus.BAD_REQUEST),
    EMPLOYEE_NOTFOUND(1001, "Employee not found", HttpStatus.BAD_REQUEST),
    WAREHOUSE_NOTFOUND(1001, "Warehouse not found", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(final int code, final String message, final HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
