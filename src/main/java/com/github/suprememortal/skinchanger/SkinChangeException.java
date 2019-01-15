package com.github.suprememortal.skinchanger;

public class SkinChangeException extends Exception {

    public SkinChangeException() {
        super();
    }

    public SkinChangeException(String message) {
        super(message);
    }

    public SkinChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkinChangeException(Throwable cause) {
        super(cause);
    }

    protected SkinChangeException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
