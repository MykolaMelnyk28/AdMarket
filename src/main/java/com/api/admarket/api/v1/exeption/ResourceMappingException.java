package com.api.admarket.api.v1.exeption;

public class ResourceMappingException extends RuntimeException {
    public ResourceMappingException(String message) {
        super(message);
    }

    public ResourceMappingException(Throwable cause) {
        super(cause);
    }
}
