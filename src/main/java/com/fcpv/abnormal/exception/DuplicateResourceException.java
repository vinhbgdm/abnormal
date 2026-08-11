package com.fcpv.abnormal.exception;

import com.fcpv.abnormal.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException {

    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE", HttpStatus.CONFLICT);
    }

    public static DuplicateResourceException of(String resource, String field, Object value) {
        return new DuplicateResourceException(
                resource + " already exists with " + field + ": " + value
        );
    }
}
