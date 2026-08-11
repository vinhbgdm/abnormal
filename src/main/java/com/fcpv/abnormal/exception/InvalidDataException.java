package com.fcpv.abnormal.exception;

import com.fcpv.abnormal.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidDataException extends BaseException {

    public InvalidDataException(String message) {
        super(message, "INVALID", HttpStatus.FORBIDDEN);
    }
}
