package com.example.medical.exceptions;

public class NoAppointmentsForDoctorException extends RuntimeException {
    public NoAppointmentsForDoctorException(String message) {
        super(message);
    }

    public NoAppointmentsForDoctorException(Integer id) {
        super("No appointments found for doctor with id " + id);
    }
}
