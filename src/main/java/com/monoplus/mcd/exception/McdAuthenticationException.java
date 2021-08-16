package com.monoplus.mcd.exception;

import org.springframework.security.core.AuthenticationException;

public class McdAuthenticationException extends AuthenticationException {
    public McdAuthenticationException(String msg) {
        super(msg);
    }
}
