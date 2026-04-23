package com.typeb.hello_world_api.service;

import com.typeb.hello_world_api.exception.InvalidInputException;
import org.springframework.stereotype.Service;


@Service
public class HelloWorldService {

    public String greet(String name) {

        String trimmed = name.strip();
        char first = trimmed.charAt(0);

        if (!Character.isLetter(first)) {
            throw new InvalidInputException("Name does not start with a letter");
        }

        char upper = Character.toUpperCase(first);

        if (upper >= 'A' && upper <= 'M') {
            return capitalize(trimmed);
        }

        throw new InvalidInputException("Name starts with a letter in the N-Z range");
    }

    private String capitalize(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}