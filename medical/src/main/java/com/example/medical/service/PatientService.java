package com.example.medical.service;

import com.example.medical.model.Patient;
import com.example.medical.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient create(Patient patient) {
        validate(patient);
        return patientRepository.save(patient);
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getById(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
    }

    public Patient update(Integer id, Patient details) {
        Patient patient = getById(id);

        patient.setFirstName(details.getFirstName());
        patient.setLastName(details.getLastName());
        patient.setEmail(details.getEmail());
        patient.setPhone(details.getPhone());
        patient.setCnp(details.getCnp());

        return patientRepository.save(patient);
    }

    public void delete(Integer id) {
        patientRepository.delete(getById(id));
    }

    private void validate(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
    }
}
