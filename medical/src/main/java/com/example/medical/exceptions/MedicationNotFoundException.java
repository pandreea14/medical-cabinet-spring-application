package com.example.medical.exceptions;

public class MedicationNotFoundException extends RuntimeException {
    public MedicationNotFoundException(String message) {
        super(message);
    }

    public MedicationNotFoundException(Integer id) {
        super("Could not find medication with id " + id);
    }
}
