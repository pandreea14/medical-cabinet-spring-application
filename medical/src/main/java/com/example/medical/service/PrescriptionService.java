package com.example.medical.service;

import com.example.medical.exceptions.EntityNotFoundException;
import com.example.medical.exceptions.MedicationNotFoundException;
import com.example.medical.exceptions.PrescriptionNotFoundException;
import com.example.medical.model.*;
import com.example.medical.repository.MedicationRepository;
import com.example.medical.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final AppointmentService appointmentService;

    public Prescription create(Integer appointmentId, String instructions) {
        Prescription prescription = new Prescription();
        prescription.setAppointment(appointmentService.getById(appointmentId));
        prescription.setIssuedDate(LocalDateTime.now());
        prescription.setInstructions(instructions);
        return prescriptionRepository.save(prescription);
    }

    public Prescription getById(Integer id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(id));
    }

    public Prescription addMedication(
            Integer prescriptionId,
            Integer medicationId,
            String dosage
    ) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new PrescriptionNotFoundException(prescriptionId));

        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException(medicationId));

        PrescriptionMedication pm = new PrescriptionMedication();
        pm.setPrescription(prescription);
        pm.setMedication(medication);
        pm.setDosage(dosage);

        prescription.getMedications().add(pm);

        return prescriptionRepository.save(prescription);
    }
}
