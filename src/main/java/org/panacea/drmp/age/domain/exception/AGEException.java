package org.panacea.drmp.age.domain.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AGEException extends RuntimeException {

    protected Throwable throwable;

    public AGEException(String message) {
        super(message);
    }

    public AGEException(String message, Throwable throwable) {
        super(message);
        this.throwable = throwable;
        log.error("[REE]: ", message);
    }

    public Throwable getCause() {
        return throwable;
    }
}