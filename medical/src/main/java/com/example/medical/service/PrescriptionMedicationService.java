package com.example.medical.service;

import com.example.medical.model.PrescriptionMedication;
import com.example.medical.repository.PrescriptionMedicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionMedicationService {
    private final PrescriptionMedicationRepository prescriptionMedicationRepository;

    public void updateDosage(Integer id, String dosage) {
        PrescriptionMedication pm = prescriptionMedicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription-Medication link not found"));
        pm.setDosage(dosage);
        prescriptionMedicationRepository.save(pm);
    }

    public void delete(Integer id) {
        prescriptionMedicationRepository.deleteById(id);
    }
}
