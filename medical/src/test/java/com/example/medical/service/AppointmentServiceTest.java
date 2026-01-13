package com.example.medical.service;

import com.example.medical.exceptions.AppointmentNotFoundException;
import com.example.medical.model.Appointment;
import com.example.medical.model.Doctor;
import com.example.medical.model.Patient;
import com.example.medical.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment testAppointment;
    private Patient testPatient;
    private Doctor testDoctor;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();

        testDoctor = Doctor.builder()
                .id(1)
                .firstName("Dr. Jane")
                .lastName("Smith")
                .build();

        testAppointment = Appointment.builder()
                .id(1)
                .patient(testPatient)
                .doctor(testDoctor)
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .reason("Regular checkup")
                .prescriptions(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Create appointment successfully")
    void create_ShouldSaveAndReturnAppointment() {
        when(patientService.getById(1)).thenReturn(testPatient);
        when(doctorService.getById(1)).thenReturn(testDoctor);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        Appointment result = appointmentService.create(testAppointment);

        assertNotNull(result);
        assertEquals(testAppointment.getId(), result.getId());
        verify(patientService, times(1)).getById(1);
        verify(doctorService, times(1)).getById(1);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Create appointment with null should throw IllegalArgumentException")
    void create_WithNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> appointmentService.create(null));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create appointment without patient ID should throw IllegalArgumentException")
    void create_WithoutPatientId_ShouldThrowException() {
        Appointment appointment = Appointment.builder()
                .patient(new Patient())
                .doctor(testDoctor)
                .build();

        assertThrows(IllegalArgumentException.class, () -> appointmentService.create(appointment));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create appointment without doctor ID should throw IllegalArgumentException")
    void create_WithoutDoctorId_ShouldThrowException() {
        Appointment appointment = Appointment.builder()
                .patient(testPatient)
                .doctor(new Doctor())
                .build();

        assertThrows(IllegalArgumentException.class, () -> appointmentService.create(appointment));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create appointment with null patient should throw IllegalArgumentException")
    void create_WithNullPatient_ShouldThrowException() {
        Appointment appointment = Appointment.builder()
                .patient(null)
                .doctor(testDoctor)
                .build();

        assertThrows(IllegalArgumentException.class, () -> appointmentService.create(appointment));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create appointment with null doctor should throw IllegalArgumentException")
    void create_WithNullDoctor_ShouldThrowException() {
        Appointment appointment = Appointment.builder()
                .patient(testPatient)
                .doctor(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> appointmentService.create(appointment));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get appointment by ID successfully")
    void getById_ShouldReturnAppointment() {
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(testAppointment));

        Appointment result = appointmentService.getById(1);

        assertNotNull(result);
        assertEquals(testAppointment.getId(), result.getId());
        verify(appointmentRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Get appointment by ID not found should throw exception")
    void getById_NotFound_ShouldThrowException() {
        when(appointmentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getById(999));
        verify(appointmentRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Get appointments by patient successfully")
    void getByPatient_ShouldReturnAppointments() {
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByPatientId(1)).thenReturn(appointments);

        List<Appointment> result = appointmentService.getByPatient(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAppointment.getId(), result.get(0).getId());
        verify(appointmentRepository, times(1)).findByPatientId(1);
    }

    @Test
    @DisplayName("Get appointments by doctor successfully")
    void getByDoctor_ShouldReturnAppointments() {
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByDoctorId(1)).thenReturn(appointments);

        List<Appointment> result = appointmentService.getByDoctor(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAppointment.getId(), result.get(0).getId());
        verify(appointmentRepository, times(1)).findByDoctorId(1);
    }

    @Test
    @DisplayName("Delete appointment successfully")
    void delete_ShouldDeleteAppointment() {
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(testAppointment));
        doNothing().when(appointmentRepository).delete(testAppointment);

        appointmentService.delete(1);

        verify(appointmentRepository, times(1)).findById(1);
        verify(appointmentRepository, times(1)).delete(testAppointment);
    }

    @Test
    @DisplayName("Delete non-existent appointment should throw exception")
    void delete_NotFound_ShouldThrowException() {
        when(appointmentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.delete(999));
        verify(appointmentRepository, times(1)).findById(999);
        verify(appointmentRepository, never()).delete(any());
    }
}

