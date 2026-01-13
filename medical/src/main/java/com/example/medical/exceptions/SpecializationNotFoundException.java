package com.example.medical.exceptions;

public class SpecializationNotFoundException extends RuntimeException {
    public SpecializationNotFoundException(String message) {
        super(message);
    }
    public SpecializationNotFoundException(Integer id) {
        super("Could not find specialization with id " + id);
    }
}
