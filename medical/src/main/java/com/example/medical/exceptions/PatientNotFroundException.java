package com.example.medical.exceptions;

public class PatientNotFroundException extends RuntimeException {
    public PatientNotFroundException(String message) {
        super(message);
    }

    public PatientNotFroundException(Integer id) {
        super("Could not find patient with id " + id);
    }
}
