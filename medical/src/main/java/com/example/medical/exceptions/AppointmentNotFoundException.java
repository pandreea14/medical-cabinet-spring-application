package com.example.medical.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
    public AppointmentNotFoundException(Integer id) {
        super("Could not find appointment with id " + id);
    }
}
