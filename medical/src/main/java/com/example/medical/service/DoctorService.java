package com.example.medical.service;

import com.example.medical.exceptions.DoctorNotFoundException;
import com.example.medical.exceptions.SpecializationNotFoundException;
import com.example.medical.model.Doctor;
import com.example.medical.model.Specialization;
import com.example.medical.repository.DoctorRepository;
import com.example.medical.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        if (doctor.getSpecialization().getName() == null || doctor.getSpecialization().getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization name is required");
        }

        Specialization specialization = specializationRepository.findByName(doctor.getSpecialization().getName())
                .orElseThrow(() -> new SpecializationNotFoundException(doctor.getSpecialization().getName()));

        doctor.setSpecialization(specialization);
        return doctorRepository.save(doctor);
    }


    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    public Doctor getById(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }


    public List<Doctor> getBySpecialization(Integer specializationId) {
        return doctorRepository.findBySpecializationId(specializationId);
    }

    public Doctor update(Integer id, Doctor doctor) {
        Doctor existing = getById(id);

        if (doctor.getSpecialization().getName() == null || doctor.getSpecialization().getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization name is required");
        }

        Specialization specialization = specializationRepository.findByName(doctor.getSpecialization().getName())
                .orElseThrow(() -> new SpecializationNotFoundException(doctor.getSpecialization().getName()));

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

