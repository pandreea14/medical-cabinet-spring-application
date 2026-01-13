package com.example.medical.exceptions;

public class PrescriptionNotFoundException extends RuntimeException {
    public PrescriptionNotFoundException(String message) {
        super(message);
    }
    public PrescriptionNotFoundException(Integer id) {
        super("Could not find prescription with id " + id);
    }
}
