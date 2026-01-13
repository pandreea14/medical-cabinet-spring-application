package com.example.medical.controller;

import com.example.medical.dto.*;
import com.example.medical.exceptions.*;
import com.example.medical.mapper.GeneralMapper;
import com.example.medical.model.*;
import com.example.medical.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalServiceController.class)
class MedicalServiceControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private PrescriptionService prescriptionService;

    @MockBean
    private PrescriptionMedicationService prescriptionMedicationService;

    @MockBean
    private MedicationService medicationService;

    @MockBean
    private SpecializationService specializationService;

    @MockBean
    private GeneralMapper mapper;

    private Patient testPatient;
    private PatientDto testPatientDto;
    private Doctor testDoctor;
    private DoctorDto testDoctorDto;
    private Appointment testAppointment;
    private AppointmentDto testAppointmentDto;
    private Prescription testPrescription;
    private PrescriptionDto testPrescriptionDto;
    private Medication testMedication;
    private MedicationDto testMedicationDto;
    private Specialization testSpecialization;
    private SpecializationDto testSpecializationDto;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        testPatientDto = PatientDto.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .appointments(new ArrayList<>())
                .build();

        testSpecialization = new Specialization();
        testSpecialization.setId(1);
        testSpecialization.setName("Cardiology");

        testDoctor = Doctor.builder()
                .id(1)
                .firstName("Dr. Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .specialization(testSpecialization)
                .build();

        testDoctorDto = DoctorDto.builder()
                .id(1)
                .firstName("Dr. Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .specialization("Cardiology")
                .appointments(new ArrayList<>())
                .build();

        testAppointment = Appointment.builder()
                .id(1)
                .patient(testPatient)
                .doctor(testDoctor)
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .reason("Regular checkup")
                .prescriptions(new ArrayList<>())
                .build();

        testAppointmentDto = AppointmentDto.builder()
                .id(1)
                .patient(testPatientDto)
                .doctor(testDoctorDto)
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .reason("Regular checkup")
                .prescriptions(new ArrayList<>())
                .build();

        testPrescription = Prescription.builder()
                .id(1)
                .appointment(testAppointment)
                .issuedDate(LocalDateTime.now())
                .instructions("Take with food")
                .medications(new ArrayList<>())
                .build();

        testPrescriptionDto = PrescriptionDto.builder()
                .id(1)
                .issuedDate(LocalDateTime.now())
                .instructions("Take with food")
                .medications(new ArrayList<>())
                .build();

        testMedication = Medication.builder()
                .id(1)
                .name("Aspirin")
                .description("Pain reliever")
                .build();

        testMedicationDto = MedicationDto.builder()
                .id(1)
                .name("Aspirin")
                .description("Pain reliever")
                .build();

        testSpecializationDto = SpecializationDto.builder()
                .id(1)
                .name("Cardiology")
                .doctors(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("POST /api/patients - Create patient successfully")
    void createPatient_ShouldReturnCreatedPatient() throws Exception {
        PatientDto requestDto = PatientDto.builder()
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        PatientDto responseDto = PatientDto.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .appointments(new ArrayList<>())
                .build();

        when(mapper.toPatient(any(PatientDto.class))).thenReturn(testPatient);
        when(patientService.create(any(Patient.class))).thenReturn(testPatient);
        when(mapper.toPatientDto(any(Patient.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(patientService, times(1)).create(any(Patient.class));
    }

    @Test
    @DisplayName("GET /api/patients - Get all patients")
    void getAllPatients_ShouldReturnPatientList() throws Exception {
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientService.getAll()).thenReturn(patients);
        when(mapper.toPatientDto(any(Patient.class))).thenReturn(testPatientDto);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(patientService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /api/patients/{id} - Get patient by ID")
    void getPatientById_ShouldReturnPatient() throws Exception {
        when(patientService.getById(1)).thenReturn(testPatient);
        when(mapper.toPatientDto(any(Patient.class))).thenReturn(testPatientDto);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(patientService, times(1)).getById(1);
    }

    @Test
    @DisplayName("GET /api/patients/{id} - Patient not found")
    void getPatientById_NotFound_ShouldReturn404() throws Exception {
        when(patientService.getById(999)).thenReturn(null);

        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());

        verify(patientService, times(1)).getById(999);
    }

    @Test
    @DisplayName("PUT /api/patients/{id} - Update patient successfully")
    void updatePatient_ShouldReturnUpdatedPatient() throws Exception {
        PatientDto requestDto = PatientDto.builder()
                .firstName("John")
                .lastName("Updated")
                .cnp("1234567890123")
                .email("john.updated@example.com")
                .phone("1234567890")
                .build();

        PatientDto responseDto = PatientDto.builder()
                .id(1)
                .firstName("John")
                .lastName("Updated")
                .cnp("1234567890123")
                .email("john.updated@example.com")
                .phone("1234567890")
                .appointments(new ArrayList<>())
                .build();

        when(mapper.toPatient(any(PatientDto.class))).thenReturn(testPatient);
        when(patientService.update(eq(1), any(Patient.class))).thenReturn(testPatient);
        when(mapper.toPatientDto(any(Patient.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("Updated"));

        verify(patientService, times(1)).update(eq(1), any(Patient.class));
    }

    @Test
    @DisplayName("DELETE /api/patients/{id} - Delete patient successfully")
    void deletePatient_ShouldReturnNoContent() throws Exception {
        doNothing().when(patientService).delete(1);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).delete(1);
    }


    @Test
    @DisplayName("POST /api/doctors - Create doctor successfully")
    void createDoctor_ShouldReturnCreatedDoctor() throws Exception {
        DoctorDto requestDto = DoctorDto.builder()
                .firstName("Dr. Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .specialization("Cardiology")
                .build();

        DoctorDto responseDto = DoctorDto.builder()
                .id(1)
                .firstName("Dr. Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .specialization("Cardiology")
                .appointments(new ArrayList<>())
                .build();

        when(mapper.toDoctor(any(DoctorDto.class))).thenReturn(testDoctor);
        when(doctorService.create(any(Doctor.class))).thenReturn(testDoctor);
        when(mapper.toDoctorDto(any(Doctor.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.specialization").value("Cardiology"));

        verify(doctorService, times(1)).create(any(Doctor.class));
    }

    @Test
    @DisplayName("GET /api/doctors - Get all doctors")
    void getAllDoctors_ShouldReturnDoctorList() throws Exception {
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorService.getAll()).thenReturn(doctors);
        when(mapper.toDoctorDto(any(Doctor.class))).thenReturn(testDoctorDto);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].specialization").value("Cardiology"));

        verify(doctorService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /api/doctors/{id} - Get doctor by ID")
    void getDoctorById_ShouldReturnDoctor() throws Exception {
        when(doctorService.getById(1)).thenReturn(testDoctor);
        when(mapper.toDoctorDto(any(Doctor.class))).thenReturn(testDoctorDto);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(doctorService, times(1)).getById(1);
    }

    @Test
    @DisplayName("GET /api/doctors/specialization/{specializationId} - Get doctors by specialization")
    void getDoctorsBySpecialization_ShouldReturnDoctorList() throws Exception {
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorService.getBySpecialization(1)).thenReturn(doctors);
        when(mapper.toDoctorDto(any(Doctor.class))).thenReturn(testDoctorDto);

        mockMvc.perform(get("/api/doctors/specialization/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(doctorService, times(1)).getBySpecialization(1);
    }

    @Test
    @DisplayName("PUT /api/doctors/{id} - Update doctor successfully")
    void updateDoctor_ShouldReturnUpdatedDoctor() throws Exception {
        DoctorDto requestDto = DoctorDto.builder()
                .firstName("Dr. Jane")
                .lastName("Updated")
                .email("jane.updated@example.com")
                .phone("0987654321")
                .specialization("Cardiology")
                .build();

        DoctorDto responseDto = DoctorDto.builder()
                .id(1)
                .firstName("Dr. Jane")
                .lastName("Updated")
                .email("jane.updated@example.com")
                .phone("0987654321")
                .specialization("Cardiology")
                .appointments(new ArrayList<>())
                .build();

        when(mapper.toDoctor(any(DoctorDto.class))).thenReturn(testDoctor);
        when(doctorService.update(eq(1), any(Doctor.class))).thenReturn(testDoctor);
        when(mapper.toDoctorDto(any(Doctor.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(doctorService, times(1)).update(eq(1), any(Doctor.class));
    }

    @Test
    @DisplayName("DELETE /api/doctors/{id} - Delete doctor successfully")
    void deleteDoctor_ShouldReturnNoContent() throws Exception {
        doNothing().when(doctorService).delete(1);

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());

        verify(doctorService, times(1)).delete(1);
    }

    @Test
    @DisplayName("POST /api/appointments - Schedule appointment successfully")
    void scheduleAppointment_ShouldReturnCreatedAppointment() throws Exception {
        AppointmentDto requestDto = AppointmentDto.builder()
                .patient(testPatientDto)
                .doctor(testDoctorDto)
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .reason("Regular checkup")
                .build();

        AppointmentDto responseDto = AppointmentDto.builder()
                .id(1)
                .patient(testPatientDto)
                .doctor(testDoctorDto)
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .reason("Regular checkup")
                .prescriptions(new ArrayList<>())
                .build();

        when(mapper.toAppointment(any(AppointmentDto.class))).thenReturn(testAppointment);
        when(appointmentService.create(any(Appointment.class))).thenReturn(testAppointment);
        when(mapper.toAppointmentDto(any(Appointment.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reason").value("Regular checkup"));

        verify(appointmentService, times(1)).create(any(Appointment.class));
    }

    @Test
    @DisplayName("GET /api/appointments/{id} - Get appointment by ID")
    void getAppointmentById_ShouldReturnAppointment() throws Exception {
        when(appointmentService.getById(1)).thenReturn(testAppointment);
        when(mapper.toAppointmentDto(any(Appointment.class))).thenReturn(testAppointmentDto);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(appointmentService, times(1)).getById(1);
    }

    @Test
    @DisplayName("GET /api/appointments/patient/{patientId} - Get appointments by patient")
    void getAppointmentsByPatient_ShouldReturnAppointmentList() throws Exception {
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(patientService.getById(1)).thenReturn(testPatient);
        when(appointmentService.getByPatient(1)).thenReturn(appointments);
        when(mapper.toAppointmentDto(any(Appointment.class))).thenReturn(testAppointmentDto);

        mockMvc.perform(get("/api/appointments/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(appointmentService, times(1)).getByPatient(1);
    }

    @Test
    @DisplayName("GET /api/appointments/patient/{patientId} - No appointments found")
    void getAppointmentsByPatient_NoAppointments_ShouldReturn404() throws Exception {
        when(patientService.getById(1)).thenReturn(testPatient);
        when(appointmentService.getByPatient(1)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/appointments/patient/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No Appointments Found"));

        verify(appointmentService, times(1)).getByPatient(1);
    }

    @Test
    @DisplayName("GET /api/appointments/doctor/{doctorId} - Get appointments by doctor")
    void getAppointmentsByDoctor_ShouldReturnAppointmentList() throws Exception {
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(doctorService.getById(1)).thenReturn(testDoctor);
        when(appointmentService.getByDoctor(1)).thenReturn(appointments);
        when(mapper.toAppointmentDto(any(Appointment.class))).thenReturn(testAppointmentDto);

        mockMvc.perform(get("/api/appointments/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(appointmentService, times(1)).getByDoctor(1);
    }

    @Test
    @DisplayName("DELETE /api/appointments/{id} - Cancel appointment successfully")
    void cancelAppointment_ShouldReturnNoContent() throws Exception {
        doNothing().when(appointmentService).delete(1);

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).delete(1);
    }

    @Test
    @DisplayName("POST /api/appointments/{appointmentId}/prescriptions - Create prescription successfully")
    void createPrescription_ShouldReturnCreatedPrescription() throws Exception {
        when(appointmentService.getById(1)).thenReturn(testAppointment);
        when(prescriptionService.create(eq(1), anyString())).thenReturn(testPrescription);
        when(mapper.toPrescriptionDto(any(Prescription.class))).thenReturn(testPrescriptionDto);

        mockMvc.perform(post("/api/appointments/1/prescriptions")
                        .param("instructions", "Take with food"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(appointmentService, times(1)).getById(1);
        verify(prescriptionService, times(1)).create(eq(1), eq("Take with food"));
    }

    @Test
    @DisplayName("GET /api/prescriptions/{id} - Get prescription by ID")
    void getPrescriptionById_ShouldReturnPrescription() throws Exception {
        when(prescriptionService.getById(1)).thenReturn(testPrescription);
        when(mapper.toPrescriptionDto(any(Prescription.class))).thenReturn(testPrescriptionDto);

        mockMvc.perform(get("/api/prescriptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(prescriptionService, times(1)).getById(1);
    }

    @Test
    @DisplayName("POST /api/prescriptions/{prescriptionId}/medications/{medicationId} - Add medication to prescription")
    void addMedicationToPrescription_ShouldReturnUpdatedPrescription() throws Exception {
        when(prescriptionService.addMedication(eq(1), eq(1), eq("2 pills daily"))).thenReturn(testPrescription);
        when(mapper.toPrescriptionDto(any(Prescription.class))).thenReturn(testPrescriptionDto);

        mockMvc.perform(post("/api/prescriptions/1/medications/1")
                        .param("dosage", "2 pills daily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(prescriptionService, times(1)).addMedication(eq(1), eq(1), eq("2 pills daily"));
    }

    @Test
    @DisplayName("POST /api/medications - Create medication successfully")
    void createMedication_ShouldReturnCreatedMedication() throws Exception {
        MedicationDto requestDto = MedicationDto.builder()
                .name("Aspirin")
                .description("Pain reliever")
                .build();

        MedicationDto responseDto = MedicationDto.builder()
                .id(1)
                .name("Aspirin")
                .description("Pain reliever")
                .build();

        when(mapper.toMedication(any(MedicationDto.class))).thenReturn(testMedication);
        when(medicationService.create(any(Medication.class))).thenReturn(testMedication);
        when(mapper.toMedicationDto(any(Medication.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Aspirin"));

        verify(medicationService, times(1)).create(any(Medication.class));
    }

    @Test
    @DisplayName("GET /api/medications - Get all medications")
    void getAllMedications_ShouldReturnMedicationList() throws Exception {
        List<Medication> medications = Arrays.asList(testMedication);
        when(medicationService.getAll()).thenReturn(medications);
        when(mapper.toMedicationDto(any(Medication.class))).thenReturn(testMedicationDto);

        mockMvc.perform(get("/api/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Aspirin"));

        verify(medicationService, times(1)).getAll();
    }

    @Test
    @DisplayName("DELETE /api/medications/{id} - Delete medication successfully")
    void deleteMedication_ShouldReturnNoContent() throws Exception {
        doNothing().when(medicationService).delete(1);

        mockMvc.perform(delete("/api/medications/1"))
                .andExpect(status().isNoContent());

        verify(medicationService, times(1)).delete(1);
    }

    @Test
    @DisplayName("POST /api/specializations - Create specialization successfully")
    void createSpecialization_ShouldReturnCreatedSpecialization() throws Exception {
        SpecializationDto requestDto = SpecializationDto.builder()
                .name("Cardiology")
                .build();

        SpecializationDto responseDto = SpecializationDto.builder()
                .id(1)
                .name("Cardiology")
                .doctors(new ArrayList<>())
                .build();

        when(mapper.toSpecialization(any(SpecializationDto.class))).thenReturn(testSpecialization);
        when(specializationService.create(any(Specialization.class))).thenReturn(testSpecialization);
        when(mapper.toSpecializationDto(any(Specialization.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/specializations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cardiology"));

        verify(specializationService, times(1)).create(any(Specialization.class));
    }

    @Test
    @DisplayName("GET /api/specializations - Get all specializations")
    void getAllSpecializations_ShouldReturnSpecializationList() throws Exception {
        List<Specialization> specializations = Arrays.asList(testSpecialization);
        when(specializationService.getAll()).thenReturn(specializations);
        when(mapper.toSpecializationDto(any(Specialization.class))).thenReturn(testSpecializationDto);

        mockMvc.perform(get("/api/specializations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cardiology"));

        verify(specializationService, times(1)).getAll();
    }

    @Test
    @DisplayName("DELETE /api/specializations/{id} - Delete specialization successfully")
    void deleteSpecialization_ShouldReturnNoContent() throws Exception {
        doNothing().when(specializationService).delete(1);

        mockMvc.perform(delete("/api/specializations/1"))
                .andExpect(status().isNoContent());

        verify(specializationService, times(1)).delete(1);
    }
}

