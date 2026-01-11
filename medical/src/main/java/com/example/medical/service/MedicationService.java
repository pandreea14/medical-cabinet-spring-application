package com.example.medical.service;

import com.example.medical.model.Medication;
import com.example.medical.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public Medication create(Medication medication) {
        if (medication == null) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
        return medicationRepository.save(medication);
    }

    public List<Medication> getAll() {
        return medicationRepository.findAll();
    }

    public Medication getById(Integer id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medication not found: " + id));
    }
}
