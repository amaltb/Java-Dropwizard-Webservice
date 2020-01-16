package com.ab.example.metastore.service.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
public class MetaStoreException extends Throwable implements Serializable {
    private static final long serialVersionUID = 21L;

    private int code;
    private final String message;


    public MetaStoreException(String message) {
        this.message = message;
    }

    public MetaStoreException(int status, String message) {
        this.message = message;
        this.code = status;
    }

    public int getStatus() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
