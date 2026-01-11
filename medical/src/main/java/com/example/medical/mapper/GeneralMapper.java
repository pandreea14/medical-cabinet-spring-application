package com.example.medical.mapper;

import com.example.medical.dto.*;
import com.example.medical.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneralMapper {

    // ========== Patient Mappings ==========

    public PatientDto toPatientDto(Patient patient) {
        if (patient == null) return null;

        return PatientDto.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .cnp(patient.getCnp())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .appointments(patient.getAppointments() != null
                    ? patient.getAppointments().stream()
                        .map(this::toAppointmentDtoWithoutPatient)
                        .collect(Collectors.toList())
                    : new ArrayList<>())
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

    // ========== Doctor Mappings ==========

    public DoctorDto toDoctorDto(Doctor doctor) {
        if (doctor == null) return null;

        return DoctorDto.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialization(doctor.getSpecialization() != null ? doctor.getSpecialization().getName() : null)
                .appointments(new ArrayList<>()) // Empty to avoid circular reference
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

        // Create a Specialization object with just the name
        // The service layer will need to validate/fetch the actual specialization
        if (doctorDto.getSpecialization() != null) {
            Specialization specialization = new Specialization();
            specialization.setName(doctorDto.getSpecialization());
            doctor.setSpecialization(specialization);
        }

        return doctor;
    }

    // ========== Specialization Mappings ==========

    public SpecializationDto toSpecializationDto(Specialization specialization) {
        if (specialization == null) return null;

        return SpecializationDto.builder()
                .id(specialization.getId())
                .name(specialization.getName())
                .doctors(specialization.getDoctors() != null
                    ? specialization.getDoctors().stream()
                        .map(this::toDoctorDto)
                        .collect(Collectors.toList())
                    : new ArrayList<>())
                .build();
    }

    public SpecializationDto toSpecializationDtoWithoutDoctors(Specialization specialization) {
        if (specialization == null) return null;

        return SpecializationDto.builder()
                .id(specialization.getId())
                .name(specialization.getName())
                .doctors(new ArrayList<>())
                .build();
    }

    public Specialization toSpecialization(SpecializationDto dto) {
        if (dto == null) return null;

        Specialization specialization = new Specialization();
        specialization.setId(dto.getId());
        specialization.setName(dto.getName());
        return specialization;
    }

    // ========== Appointment Mappings ==========

    public AppointmentDto toAppointmentDto(Appointment appointment) {
        if (appointment == null) return null;

        return AppointmentDto.builder()
                .id(appointment.getId())
                .patient(toPatientDto(appointment.getPatient()))
                .doctor(toDoctorDto(appointment.getDoctor()))
                .appointmentDate(appointment.getAppointmentDate())
                .reason(appointment.getReason())
                .prescription(toPrescriptionDtoWithoutAppointment(appointment.getPrescription()))
                .build();
    }

    // Version without patient to break circular reference
    private AppointmentDto toAppointmentDtoWithoutPatient(Appointment appointment) {
        if (appointment == null) return null;

        return AppointmentDto.builder()
                .id(appointment.getId())
                .patient(null) // Break circular reference
                .doctor(toDoctorDto(appointment.getDoctor()))
                .appointmentDate(appointment.getAppointmentDate())
                .reason(appointment.getReason())
                .prescription(toPrescriptionDtoWithoutAppointment(appointment.getPrescription()))
                .build();
    }

    public Appointment toAppointment(AppointmentDto dto) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());
        return appointment;
    }

    // ========== Prescription Mappings ==========

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

    // Version without appointment to break circular reference
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

    public Prescription toPrescription(PrescriptionDto dto) {
        if (dto == null) return null;

        Prescription prescription = new Prescription();
        prescription.setId(dto.getId());
        prescription.setIssuedDate(dto.getIssuedDate());
        prescription.setInstructions(dto.getInstructions());
        return prescription;
    }

    // ========== Medication Mappings ==========

    public MedicationDto toMedicationDto(Medication medication) {
        if (medication == null) return null;

        return MedicationDto.builder()
                .id(medication.getId())
                .name(medication.getName())
                .description(medication.getDescription())
                .build();
    }

    public Medication toMedication(MedicationDto dto) {
        if (dto == null) return null;

        Medication medication = new Medication();
        medication.setId(dto.getId());
        medication.setName(dto.getName());
        medication.setDescription(dto.getDescription());
        return medication;
    }

    // ========== PrescriptionMedication Mappings ==========

    public PrescriptionMedicationDto toPrescriptionMedicationDto(PrescriptionMedication pm) {
        if (pm == null) return null;

        return PrescriptionMedicationDto.builder()
                .medication(toMedicationDto(pm.getMedication()))
                .dosage(pm.getDosage())
                .prescription(null) // Break circular reference
                .build();
    }

    // Version without prescription to break circular reference
    private PrescriptionMedicationDto toPrescriptionMedicationDtoWithoutPrescription(PrescriptionMedication pm) {
        if (pm == null) return null;

        return PrescriptionMedicationDto.builder()
                .medication(toMedicationDto(pm.getMedication()))
                .dosage(pm.getDosage())
                .prescription(null)
                .build();
    }

    public PrescriptionMedication toPrescriptionMedication(PrescriptionMedicationDto dto) {
        if (dto == null) return null;

        PrescriptionMedication pm = new PrescriptionMedication();
        pm.setDosage(dto.getDosage());
        return pm;
    }
}

