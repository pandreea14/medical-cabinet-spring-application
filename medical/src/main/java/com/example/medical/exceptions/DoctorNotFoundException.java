package com.example.medical.exceptions;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
    public DoctorNotFoundException(Integer id) {
        super("Could not find doctor with id " + id);
    }
}
