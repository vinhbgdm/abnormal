package com.fcpv.abnormal.exception;

import com.fcpv.abnormal.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message, "FORBIDDEN", HttpStatus.FORBIDDEN);
    }
}
