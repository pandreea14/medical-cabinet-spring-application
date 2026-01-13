package com.example.medical.exceptions;

public class NoAppointmentsForPatientException extends RuntimeException {
    public NoAppointmentsForPatientException(String message) {
        super(message);
    }

    public NoAppointmentsForPatientException(Integer patientId) {
        super("No appointments found for patient with id " + patientId);
    }
}

