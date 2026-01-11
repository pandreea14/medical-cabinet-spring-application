package com.example.medical.controller;

import com.example.medical.dto.*;
import com.example.medical.mapper.GeneralMapper;
import com.example.medical.model.*;
import com.example.medical.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Validated
public class MedicalServiceController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final PrescriptionMedicationService prescriptionMedicationService;
    private final GeneralMapper mapper;

    @Tag(name = "Patients", description = "Patient management endpoints")
    @PostMapping("/patients")
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto patientDto) {
        Patient patient = mapper.toPatient(patientDto);
        Patient savedPatient = patientService.create(patient);
        return new ResponseEntity<>(mapper.toPatientDto(savedPatient), HttpStatus.CREATED);
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<Patient> patients = patientService.getAll();
        List<PatientDto> patientDtos = new ArrayList<>();
        for (Patient patient : patients) {
            patientDtos.add(mapper.toPatientDto(patient));
        }
        return ResponseEntity.ok(patientDtos);
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Integer id) {
        Patient patient = patientService.getById(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toPatientDto(patient));
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDto patientDto) {
        try {
            Patient patientDetails = mapper.toPatient(patientDto);
            Patient updatedPatient = patientService.update(id, patientDetails);
            return ResponseEntity.ok(mapper.toPatientDto(updatedPatient));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        try {
            patientService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAll();
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDtos.add(mapper.toDoctorDto(doctor));
        }
        return ResponseEntity.ok(doctorDtos);
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Integer id) {
        Doctor doc = doctorService.getById(id);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDoctorDto(doc));
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @GetMapping("/doctors/specialization/{specializationId}")
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialization(@PathVariable Integer specializationId) {
        List<Doctor> doctors = doctorService.getBySpecialization(specializationId);
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDtos.add(mapper.toDoctorDto(doctor));
        }
        return ResponseEntity.ok(doctorDtos);
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @PostMapping("/doctors")
    public ResponseEntity<DoctorDto> createDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        Doctor doctor = mapper.toDoctor(doctorDto);
        Doctor savedDoctor = doctorService.create(doctor);
        return new ResponseEntity<>(mapper.toDoctorDto(savedDoctor), HttpStatus.CREATED);
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Integer id, @Valid @RequestBody DoctorDto doctorDto) {
        try {
            Doctor doctorDetails = mapper.toDoctor(doctorDto);
            Doctor updatedDoctor = doctorService.update(id, doctorDetails);
            return ResponseEntity.ok(mapper.toDoctorDto(updatedDoctor));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer id) {
        try {
            doctorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentDto> scheduleAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        try {
            Appointment appointment = mapper.toAppointment(appointmentDto);
            Appointment savedAppointment = appointmentService.create(appointment);
            return new ResponseEntity<>(mapper.toAppointmentDto(savedAppointment), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @GetMapping("/appointments/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Integer id) {
        Appointment appointment = appointmentService.getById(id);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toAppointmentDto(appointment));
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @GetMapping("/appointments/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatient(@PathVariable Integer patientId) {
        List<Appointment> appointments = appointmentService.getByPatient(patientId);
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(mapper.toAppointmentDto(appointment));
        }
        return ResponseEntity.ok(appointmentDtos);
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @GetMapping("/appointments/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctor(@PathVariable Integer doctorId) {
        List<Appointment> appointments = appointmentService.getByDoctor(doctorId);
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(mapper.toAppointmentDto(appointment));
        }
        return ResponseEntity.ok(appointmentDtos);
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@Parameter(
            description = "ID of the appointment to delete",
            required = true) @PathVariable Integer id) {
        try {
            appointmentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @PostMapping("/appointments/{appointmentId}/prescriptions")
    @Operation(
            summary = "Create a new prescription for an appointment",
            description = "Creates a new prescription linked to an existing appointment with optional instructions"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Prescription created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found to link prescription to",
                    content = @Content
            )
    })
    public ResponseEntity<PrescriptionDto> createPrescription(
            @Parameter(description = "ID of the appointment", required = true)
            @PathVariable Integer appointmentId,
            @Parameter(description = "Optional instructions for the prescription")
            @RequestParam(required = false) String instructions) {
        Prescription savedPrescription = prescriptionService.create(appointmentId, instructions);
        return new ResponseEntity<>(mapper.toPrescriptionDto(savedPrescription), HttpStatus.CREATED);
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable Integer id) {
        Prescription prescription = prescriptionService.getById(id);
        if (prescription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toPrescriptionDto(prescription));
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @PostMapping("/prescriptions/{prescriptionId}/medications/{medicationId}")
    public ResponseEntity<PrescriptionDto> addMedicationToPrescription(
            @PathVariable Integer prescriptionId,
            @PathVariable Integer medicationId,
            @RequestParam String dosage) {
        try {
            Prescription prescription = prescriptionService.addMedication(prescriptionId, medicationId, dosage);
            return ResponseEntity.ok(mapper.toPrescriptionDto(prescription));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @PutMapping("/prescription-medications/{id}/dosage")
    public ResponseEntity<Void> updateDosage(
            @PathVariable Integer id,
            @RequestParam String dosage) {
        prescriptionMedicationService.updateDosage(id, dosage);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @DeleteMapping("/prescription-medications/{id}")
    public ResponseEntity<Void> removeMedicationFromPrescription(@PathVariable Integer id) {
        try {
            prescriptionMedicationService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
//
//    @GetMapping("/prescriptions")
//    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions() {
//        List<Prescription> prescriptions = medicalService.getAllPrescriptions();
//        List<PrescriptionDto> prescriptionDtos = new ArrayList<>();
//        for (Prescription prescription : prescriptions) {
//            prescriptionDtos.add(mapper.toPrescriptionDto(prescription));
//        }
//        return ResponseEntity.ok(prescriptionDtos);
//    }
//
//
//
//    // ==================== Medication Endpoints ====================
//
//    @PostMapping("/medications")
//    public ResponseEntity<MedicationDto> createMedication(@RequestBody MedicationDto medicationDto) {
//        Medication medication = mapper.toMedication(medicationDto);
//        Medication savedMedication = medicalService.createMedication(medication);
//        return new ResponseEntity<>(mapper.toMedicationDto(savedMedication), HttpStatus.CREATED);
//    }
//
//    @GetMapping("/medications")
//    public ResponseEntity<List<MedicationDto>> getAllMedications() {
//        List<Medication> medications = medicalService.getAllMedications();
//        List<MedicationDto> medicationDtos = new ArrayList<>();
//        for (Medication medication : medications) {
//            medicationDtos.add(mapper.toMedicationDto(medication));
//        }
//        return ResponseEntity.ok(medicationDtos);
//    }
//
//    @GetMapping("/medications/{id}")
//    public ResponseEntity<MedicationDto> getMedicationById(@PathVariable Integer id) {
//        Optional<Medication> medication = medicalService.getMedicationById(id);
//        return medication.map(value -> ResponseEntity.ok(mapper.toMedicationDto(value)))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/medications/{id}")
//    public ResponseEntity<MedicationDto> updateMedication(@PathVariable Integer id, @RequestBody MedicationDto medicationDto) {
//        try {
//            Medication medicationDetails = mapper.toMedication(medicationDto);
//            Medication updatedMedication = medicalService.updateMedication(id, medicationDetails);
//            return ResponseEntity.ok(mapper.toMedicationDto(updatedMedication));
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/medications/{id}")
//    public ResponseEntity<Void> deleteMedication(@PathVariable Integer id) {
//        try {
//            medicalService.deleteMedication(id);
//            return ResponseEntity.noContent().build();
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // ==================== Specialization Endpoints ====================
//
//    @GetMapping("/specializations")
//    public ResponseEntity<List<SpecializationDto>> getAllSpecializations() {
//        List<Specialization> specializations = medicalService.getAllSpecializations();
//        List<SpecializationDto> specializationDtos = new ArrayList<>();
//        for (Specialization specialization : specializations) {
//            specializationDtos.add(mapper.toSpecializationDto(specialization));
//        }
//        return ResponseEntity.ok(specializationDtos);
//    }
//
//    @GetMapping("/specializations/{id}")
//    public ResponseEntity<SpecializationDto> getSpecializationById(@PathVariable Integer id) {
//        Optional<Specialization> specialization = medicalService.getSpecializationById(id);
//        return specialization.map(value -> ResponseEntity.ok(mapper.toSpecializationDto(value)))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/specializations")
//    public ResponseEntity<SpecializationDto> createSpecialization(@RequestBody SpecializationDto specializationDto) {
//        Specialization specialization = mapper.toSpecialization(specializationDto);
//        Specialization savedSpecialization = medicalService.createSpecialization(specialization);
//        return new ResponseEntity<>(mapper.toSpecializationDto(savedSpecialization), HttpStatus.CREATED);
//    }
//}
