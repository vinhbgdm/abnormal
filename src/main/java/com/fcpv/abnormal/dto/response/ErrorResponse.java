package com.fcpv.abnormal.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;  // Only for validation failures

    public static ErrorResponse of(String message, String errorCode,
                                   String path) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse validation(Map<String, String> errors, String path) {
        return ErrorResponse.builder()
                .success(false)
                .message("Validation failed")
                .errorCode("VALIDATION_ERROR")
                .path(path)
                .timestamp(LocalDateTime.now())
                .validationErrors(errors)
                .build();
    }
}