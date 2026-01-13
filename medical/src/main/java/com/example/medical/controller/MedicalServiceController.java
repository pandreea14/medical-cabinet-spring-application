package com.example.medical.controller;

import com.example.medical.dto.*;
import com.example.medical.exceptions.*;
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
    private final MedicationService medicationService;
    private final SpecializationService specializationService;

    @Tag(name = "Patients", description = "Patient management endpoints")
    @PostMapping("/patients")
    @Operation(
            summary = "Create a new patient",
            description = "Registers a new patient in the system with all required details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Patient created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid patient data provided",
                    content = @Content
            )
    })
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto patientDto) {
        Patient patient = mapper.toPatient(patientDto);
        Patient savedPatient = patientService.create(patient);
        return new ResponseEntity<>(mapper.toPatientDto(savedPatient), HttpStatus.CREATED);
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @GetMapping("/patients")
    @Operation(
            summary = "Get all patients",
            description = "Retrieves a list of all registered patients in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of patients retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))
            )
    })
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<Patient> patients = patientService.getAll();
        List<PatientDto> patientDtos = new ArrayList<>();
        for (Patient patient : patients) {
            patientDtos.add(mapper.toPatientDto(patient));
        }
        return new ResponseEntity<>(patientDtos, HttpStatus.OK);
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @GetMapping("/patients/{id}")
    @Operation(
            summary = "Get patient by ID",
            description = "Retrieves detailed information about a specific patient"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found",
                    content = @Content
            )
    })
    public ResponseEntity<PatientDto> getPatientById(
            @Parameter(description = "ID of the patient to retrieve", required = true)
            @PathVariable Integer id) {
        Patient patient = patientService.getById(id);
        if (patient == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.toPatientDto(patient), HttpStatus.OK);
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @PutMapping("/patients/{id}")
    @Operation(
            summary = "Update patient information",
            description = "Updates the details of an existing patient"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid patient data",
                    content = @Content
            )
    })
    public ResponseEntity<PatientDto> updatePatient(
            @Parameter(description = "ID of the patient to update", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated patient details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PatientDto.class))
            )
            @Valid @RequestBody PatientDto patientDto) {
        try {
            Patient patientDetails = mapper.toPatient(patientDto);
            Patient updatedPatient = patientService.update(id, patientDetails);
            return ResponseEntity.ok(mapper.toPatientDto(updatedPatient));
        } catch (PatientNotFroundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Patients", description = "Patient management endpoints")
    @DeleteMapping("/patients/{id}")
    @Operation(
            summary = "Delete patient",
            description = "Removes a patient from the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Patient deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID of the patient to delete", required = true)
            @PathVariable Integer id) {
        try {
            patientService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PatientNotFroundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @GetMapping("/doctors")
    @Operation(
            summary = "Get all doctors",
            description = "Retrieves a list of all doctors registered in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of doctors retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))
            )
    })
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAll();
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDtos.add(mapper.toDoctorDto(doctor));
        }
        return new ResponseEntity<>(doctorDtos, HttpStatus.OK);
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @GetMapping("/doctors/{id}")
    @Operation(
            summary = "Get doctor by ID",
            description = "Retrieves detailed information about a specific doctor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Doctor found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Doctor not found",
                    content = @Content
            )
    })
    public ResponseEntity<DoctorDto> getDoctorById(
            @Parameter(description = "ID of the doctor to retrieve", required = true)
            @PathVariable Integer id) {
        Doctor doc = doctorService.getById(id);
        if (doc == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.toDoctorDto(doc), HttpStatus.OK);
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @GetMapping("/doctors/specialization/{specializationId}")
    @Operation(
            summary = "Get doctors by specialization",
            description = "Retrieves all doctors with a specific medical specialization"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Doctors retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specialization not found",
                    content = @Content
            )
    })
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialization(
            @Parameter(description = "ID of the specialization", required = true)
            @PathVariable Integer specializationId) {
        List<Doctor> doctors = doctorService.getBySpecialization(specializationId);
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDtos.add(mapper.toDoctorDto(doctor));
        }
        return new ResponseEntity<>(doctorDtos, HttpStatus.OK);
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @PostMapping("/doctors")
    @Operation(
            summary = "Create a new doctor",
            description = "Registers a new doctor in the system with specialization details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Doctor created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid doctor data provided",
                    content = @Content
            )
    })
    public ResponseEntity<DoctorDto> createDoctor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Doctor details including specialization",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DoctorDto.class))
            )
            @Valid @RequestBody DoctorDto doctorDto) {
        Doctor doctor = mapper.toDoctor(doctorDto);
        Doctor savedDoctor = doctorService.create(doctor);
        return new ResponseEntity<>(mapper.toDoctorDto(savedDoctor), HttpStatus.CREATED);
    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @PutMapping("/doctors/{id}")
    @Operation(
            summary = "Update doctor information",
            description = "Updates the details of an existing doctor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Doctor updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Doctor not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid doctor data",
                    content = @Content
            )
    })
    public ResponseEntity<DoctorDto> updateDoctor(
            @Parameter(description = "ID of the doctor to update", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated doctor details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DoctorDto.class))
            )
            @Valid @RequestBody DoctorDto doctorDto) {
        try {
            doctorService.getById(id);
        } catch (DoctorNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Doctor doctorDetails = mapper.toDoctor(doctorDto);
        Doctor updatedDoctor = doctorService.update(id, doctorDetails);
        return ResponseEntity.ok(mapper.toDoctorDto(updatedDoctor));

    }

    @Tag(name = "Doctors", description = "Doctor management endpoints")
    @DeleteMapping("/doctors/{id}")
    @Operation(
            summary = "Delete doctor",
            description = "Removes a doctor from the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Doctor deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Doctor not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteDoctor(
            @Parameter(description = "ID of the doctor to delete", required = true)
            @PathVariable Integer id) {
        try {
            doctorService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DoctorNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @PostMapping("/appointments")
    @Operation(
            summary = "Schedule a new appointment",
            description = "Creates a new appointment between a patient and a doctor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment scheduled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid appointment data or scheduling conflict",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient or doctor not found",
                    content = @Content
            )
    })
    public ResponseEntity<AppointmentDto> scheduleAppointment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Appointment details including patient ID, doctor ID, and date/time",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AppointmentDto.class))
            )
            @Valid @RequestBody AppointmentDto appointmentDto) {

        Appointment appointment = mapper.toAppointment(appointmentDto);
        Appointment savedAppointment = appointmentService.create(appointment);
        return new ResponseEntity<>(mapper.toAppointmentDto(savedAppointment), HttpStatus.CREATED);

    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @GetMapping("/appointments/{id}")
    @Operation(
            summary = "Get appointment by ID",
            description = "Retrieves detailed information about a specific appointment"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found",
                    content = @Content
            )
    })
    public ResponseEntity<AppointmentDto> getAppointmentById(
            @Parameter(description = "ID of the appointment to retrieve", required = true)
            @PathVariable Integer id) {
        try {
            Appointment appointment = appointmentService.getById(id);
            return ResponseEntity.ok(mapper.toAppointmentDto(appointment));
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @GetMapping("/appointments/patient/{patientId}")
    @Operation(
            summary = "Get appointments by patient",
            description = "Retrieves all appointments for a specific patient"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient appointments retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found",
                    content = @Content
            )
    })
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatient(
            @Parameter(description = "ID of the patient", required = true)
            @PathVariable Integer patientId) {

        patientService.getById(patientId);

        List<Appointment> appointments = appointmentService.getByPatient(patientId);

        if (appointments == null || appointments.isEmpty()) {
            throw new NoAppointmentsForPatientException(patientId);
        }

        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(mapper.toAppointmentDto(appointment));
        }
        return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @GetMapping("/appointments/doctor/{doctorId}")
    @Operation(
            summary = "Get appointments by doctor",
            description = "Retrieves all appointments for a specific doctor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Doctor appointments retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Doctor not found",
                    content = @Content
            )
    })
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctor(
            @Parameter(description = "ID of the doctor", required = true)
            @PathVariable Integer doctorId) {
        doctorService.getById(doctorId);

        List<Appointment> appointments = appointmentService.getByDoctor(doctorId);

        if (appointments == null || appointments.isEmpty()) {
            throw new NoAppointmentsForDoctorException(doctorId);
        }

        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(mapper.toAppointmentDto(appointment));
        }
        return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
    }

    @Tag(name = "Appointments", description = "Appointment management endpoints")
    @DeleteMapping("/appointments/{id}")
    @Operation(
            summary = "Cancel appointment",
            description = "Cancels and removes an appointment from the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Appointment cancelled successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteAppointment(
            @Parameter(description = "ID of the appointment to delete", required = true)
            @PathVariable Integer id) {
        try {
            appointmentService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        try {
            appointmentService.getById(appointmentId);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Prescription savedPrescription = prescriptionService.create(appointmentId, instructions);
        return new ResponseEntity<>(mapper.toPrescriptionDto(savedPrescription), HttpStatus.CREATED);
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @GetMapping("/prescriptions/{id}")
    @Operation(
            summary = "Get prescription by ID",
            description = "Retrieves a prescription with all its medications and dosage information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Prescription found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prescription not found",
                    content = @Content
            )
    })
    public ResponseEntity<PrescriptionDto> getPrescriptionById(
            @Parameter(description = "ID of the prescription", required = true)
            @PathVariable Integer id) {
        try {
            Prescription prescription = prescriptionService.getById(id);
            return new ResponseEntity<>(mapper.toPrescriptionDto(prescription), HttpStatus.OK);
        } catch (PrescriptionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @PostMapping("/prescriptions/{prescriptionId}/medications/{medicationId}")
    @Operation(
            summary = "Add medication to prescription",
            description = "Adds a medication with specified dosage instructions to an existing prescription"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Medication added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prescription or medication not found",
                    content = @Content
            )
    })
    public ResponseEntity<PrescriptionDto> addMedicationToPrescription(
            @Parameter(description = "ID of the prescription", required = true)
            @PathVariable Integer prescriptionId,
            @Parameter(description = "ID of the medication to add", required = true)
            @PathVariable Integer medicationId,
            @Parameter(description = "Dosage instructions (e.g., '2 tablets twice daily with meals')", required = true)
            @RequestParam String dosage) {
        try {
            Prescription prescription = prescriptionService.addMedication(prescriptionId, medicationId, dosage);
            return ResponseEntity.ok(mapper.toPrescriptionDto(prescription));
        } catch (PrescriptionNotFoundException | MedicationNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @PutMapping("/prescription-medications/{id}/dosage")
    @Operation(
            summary = "Update medication dosage",
            description = "Updates the dosage instructions for a medication in an existing prescription"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dosage updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prescription-medication link not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> updateDosage(
            @Parameter(description = "ID of the prescription-medication association", required = true)
            @PathVariable Integer id,
            @Parameter(description = "New dosage instructions", required = true)
            @RequestParam String dosage) {
        try {
            prescriptionMedicationService.updateDosage(id, dosage);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PrescriptionMedicationEntryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Prescriptions", description = "Prescription management endpoints")
    @DeleteMapping("/prescription-medications/{id}")
    @Operation(
            summary = "Remove medication from prescription",
            description = "Removes a specific medication entry from a prescription"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Medication removed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prescription-medication link not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> removeMedicationFromPrescription(
            @Parameter(description = "ID of the prescription-medication association to remove", required = true)
            @PathVariable Integer id) {
        try {
            prescriptionMedicationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PrescriptionMedicationEntryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Medications", description = "Medications management endpoints")
    @GetMapping("/medications")
    public ResponseEntity<List<MedicationDto>> getAllMedications() {
        List<Medication> medications = medicationService.getAll();
        List<MedicationDto> medicationDtos = new ArrayList<>();
        for (Medication medication : medications) {
            medicationDtos.add(mapper.toMedicationDto(medication));
        }
        return ResponseEntity.ok(medicationDtos);
    }

    @Tag(name = "Medications", description = "Medications management endpoints")
    @PostMapping("/medications")
    public ResponseEntity<MedicationDto> createMedication(@Valid @RequestBody MedicationDto medicationDto) {
        Medication medication = mapper.toMedication(medicationDto);
        Medication savedMedication = medicationService.create(medication);
        return new ResponseEntity<>(mapper.toMedicationDto(savedMedication), HttpStatus.CREATED);
    }

    @Tag(name = "Medications", description = "Medications management endpoints")
    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Integer id) {
        try {
            medicationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MedicationNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Specializations", description = "Medical Specializations management endpoints")
    @GetMapping("/specializations")
    public ResponseEntity<List<SpecializationDto>> getAllSpecializations() {
        List<Specialization> specializations = specializationService.getAll();
        List<SpecializationDto> specializationDtos = new ArrayList<>();
        for (Specialization specialization : specializations) {
            specializationDtos.add(mapper.toSpecializationDto(specialization));
        }
        return new ResponseEntity<>(specializationDtos, HttpStatus.OK);
    }

    @Tag(name = "Specializations", description = "Medical Specializations management endpoints")
    @PostMapping("/specializations")
    public ResponseEntity<SpecializationDto> createSpecialization(@Valid @RequestBody SpecializationDto specializationDto) {
        Specialization specialization = mapper.toSpecialization(specializationDto);
        Specialization savedSpecialization = specializationService.create(specialization);
        return new ResponseEntity<>(mapper.toSpecializationDto(savedSpecialization), HttpStatus.CREATED);
    }

    @Tag(name = "Specializations", description = "Medical Specializations management endpoints")
    @DeleteMapping("/specializations/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Integer id) {
        try {
            specializationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (SpecializationNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}