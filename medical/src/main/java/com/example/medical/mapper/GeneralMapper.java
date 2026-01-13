package com.example.medical.mapper;

import com.example.medical.dto.*;
import com.example.medical.model.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneralMapper {
    public PatientDto toPatientDto(Patient patient) {
        if (patient == null) return null;

        return PatientDto.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .cnp(patient.getCnp())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .appointments(new ArrayList<>())
                .build();
    }

    public Patient toPatient(PatientDto patientDto) {
        if (patientDto == null) return null;

        return Patient.builder()
                .id(patientDto.getId())
                .firstName(patientDto.getFirstName())
                .lastName(patientDto.getLastName())
                .cnp(patientDto.getCnp())
                .email(patientDto.getEmail())
                .phone(patientDto.getPhone())
                .build();
    }

    public DoctorDto toDoctorDto(Doctor doctor) {
        if (doctor == null) return null;

        return DoctorDto.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialization(doctor.getSpecialization() != null ? doctor.getSpecialization().getName() : null)
                .appointments(new ArrayList<>())
                .build();
    }

    public Doctor toDoctor(DoctorDto doctorDto) {
        if (doctorDto == null) return null;

        Doctor doctor = new Doctor();
        doctor.setId(doctorDto.getId());
        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setPhone(doctorDto.getPhone());

        if (doctorDto.getSpecialization() != null && !doctorDto.getSpecialization().trim().isEmpty()) {
            Specialization specialization = new Specialization();
            specialization.setName(doctorDto.getSpecialization());
            doctor.setSpecialization(specialization);
        }

        return doctor;
    }

    public AppointmentDto toAppointmentDto(Appointment appointment) {
        if (appointment == null) return null;

        PatientDto patientDto = appointment.getPatient() != null ? PatientDto.builder()
                .id(appointment.getPatient().getId())
                .firstName(appointment.getPatient().getFirstName())
                .lastName(appointment.getPatient().getLastName())
                .cnp(appointment.getPatient().getCnp())
                .email(appointment.getPatient().getEmail())
                .phone(appointment.getPatient().getPhone())
                .appointments(new ArrayList<>())
                .build() : null;

        return AppointmentDto.builder()
                .id(appointment.getId())
                .patient(patientDto)
                .doctor(toDoctorDto(appointment.getDoctor()))
                .appointmentDate(appointment.getAppointmentDate())
                .reason(appointment.getReason())
                .prescriptions(appointment.getPrescriptions() != null
                        ? appointment.getPrescriptions().stream()
                        .map(this::toPrescriptionDtoWithoutAppointment)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public Appointment toAppointment(AppointmentDto dto) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());

        if (dto.getPatient() != null && dto.getPatient().getId() != null) {
            Patient patient = new Patient();
            patient.setId(dto.getPatient().getId());
            appointment.setPatient(patient);
        }

        if (dto.getDoctor() != null && dto.getDoctor().getId() != null) {
            Doctor doctor = new Doctor();
            doctor.setId(dto.getDoctor().getId());
            appointment.setDoctor(doctor);
        }

        return appointment;
    }

    public PrescriptionDto toPrescriptionDto(Prescription prescription) {
        if (prescription == null) return null;

        return PrescriptionDto.builder()
                .id(prescription.getId())
                .issuedDate(prescription.getIssuedDate())
                .instructions(prescription.getInstructions())
                .medications(prescription.getMedications() != null
                        ? prescription.getMedications().stream()
                        .map(this::toPrescriptionMedicationDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    private PrescriptionDto toPrescriptionDtoWithoutAppointment(Prescription prescription) {
        if (prescription == null) return null;

        return PrescriptionDto.builder()
                .id(prescription.getId())
                .issuedDate(prescription.getIssuedDate())
                .instructions(prescription.getInstructions())
                .medications(prescription.getMedications() != null
                        ? prescription.getMedications().stream()
                        .map(this::toPrescriptionMedicationDtoWithoutPrescription)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public MedicationDto toMedicationDto(Medication medication) {
        if (medication == null) return null;

        return MedicationDto.builder()
                .id(medication.getId())
                .name(medication.getName())
                .description(medication.getDescription())
                .build();
    }

    public PrescriptionMedicationDto toPrescriptionMedicationDto(PrescriptionMedication pm) {
        if (pm == null) return null;

        return PrescriptionMedicationDto.builder()
                .medication(toMedicationDto(pm.getMedication()))
                .dosage(pm.getDosage())
                .prescription(null)
                .build();
    }

    private PrescriptionMedicationDto toPrescriptionMedicationDtoWithoutPrescription(PrescriptionMedication pm) {
        if (pm == null) return null;

        return PrescriptionMedicationDto.builder()
                .medication(toMedicationDto(pm.getMedication()))
                .dosage(pm.getDosage())
                .prescription(null)
                .build();
    }

    public Medication toMedication(@Valid MedicationDto medicationDto) {
        if (medicationDto == null) return null;

        return Medication.builder()
                .id(medicationDto.getId())
                .name(medicationDto.getName())
                .description(medicationDto.getDescription())
                .build();
    }


    public SpecializationDto toSpecializationDto(Specialization specialization) {
        if (specialization == null) return null;

        return SpecializationDto.builder()
                .id(specialization.getId())
                .name(specialization.getName())
                .build();
    }


    public Specialization toSpecialization(@Valid SpecializationDto specializationDto) {
        if (specializationDto == null) return null;

        return Specialization.builder()
                .id(specializationDto.getId())
                .name(specializationDto.getName())
                .build();
    }
}

