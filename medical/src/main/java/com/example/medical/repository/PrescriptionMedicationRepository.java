package com.example.medical.repository;

import com.example.medical.model.PrescriptionMedication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionMedicationRepository extends JpaRepository<PrescriptionMedication, Integer> {
}
