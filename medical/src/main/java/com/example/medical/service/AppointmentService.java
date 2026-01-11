package com.example.medical.service;

import com.example.medical.model.Appointment;
import com.example.medical.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public Appointment create(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }

        appointment.setPatient(
                patientService.getById(appointment.getPatient().getId())
        );
        appointment.setDoctor(
                doctorService.getById(appointment.getDoctor().getId())
        );

        return appointmentRepository.save(appointment);
    }

    public Appointment getById(Integer id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
    }

    public List<Appointment> getByPatient(Integer patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getByDoctor(Integer doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public void delete(Integer id) {
        appointmentRepository.delete(getById(id));
    }
}
