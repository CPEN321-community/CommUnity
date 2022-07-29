package com.example.community.exceptions;

public class PreferenceException extends Exception {
    public PreferenceException() {
        super();
    }

    public PreferenceException(String message) {
        super(message);
    }

    public PreferenceException(Throwable cause) {
        super(cause);
    }

    public PreferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
