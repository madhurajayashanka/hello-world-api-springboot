package com.typeb.hello_world_api.exception;


public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}