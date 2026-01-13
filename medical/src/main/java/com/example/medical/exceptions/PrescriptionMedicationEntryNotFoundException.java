package com.example.medical.exceptions;

public class PrescriptionMedicationEntryNotFoundException extends RuntimeException {
    public PrescriptionMedicationEntryNotFoundException(String message) {
        super(message);
    }

    public PrescriptionMedicationEntryNotFoundException(Integer id) {
        super("Could not find prescription medication entry with id " + id);
    }
}
