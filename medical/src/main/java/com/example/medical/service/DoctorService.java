package com.example.medical.service;

import com.example.medical.model.Doctor;
import com.example.medical.model.Specialization;
import com.example.medical.repository.DoctorRepository;
import com.example.medical.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.medical.exceptions.ResourceNotFoundException;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;

    public Doctor create(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }

        if (doctor.getFirstName() == null || doctor.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name is required");
        }

        if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor email is required");
        }

        // Specialization is required (NOT NULL in database)
        if (doctor.getSpecialization() == null || doctor.getSpecialization().getName() == null) {
            throw new IllegalArgumentException("Specialization is required");
        }

        // Verify specialization exists by name and get the full entity
        Specialization specialization = specializationRepository.findByName(doctor.getSpecialization().getName())
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found: " + doctor.getSpecialization().getName()));

        doctor.setSpecialization(specialization);
        return doctorRepository.save(doctor);
    }


    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    public Doctor getById(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }


    public List<Doctor> getBySpecialization(Integer specializationId) {
        return doctorRepository.findBySpecializationId(specializationId);
    }

    public Doctor update(Integer id, Doctor doctor) {
        Doctor existing = getById(id);

        // Specialization is required (NOT NULL in database)
        if (doctor.getSpecialization() == null || doctor.getSpecialization().getName() == null) {
            throw new IllegalArgumentException("Specialization is required");
        }

        // Verify specialization exists by name and get the full entity
        Specialization specialization = specializationRepository.findByName(doctor.getSpecialization().getName())
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found: " + doctor.getSpecialization().getName()));

        existing.setFirstName(doctor.getFirstName());
        existing.setLastName(doctor.getLastName());
        existing.setEmail(doctor.getEmail());
        existing.setPhone(doctor.getPhone());
        existing.setSpecialization(specialization);
        existing.setAppointments(doctor.getAppointments());

        return doctorRepository.save(existing);
    }


    public void delete(Integer id) {
        doctorRepository.delete(getById(id));
    }
}

