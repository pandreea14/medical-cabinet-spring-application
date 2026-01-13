package com.example.medical.service;

import com.example.medical.exceptions.MedicationNotFoundException;
import com.example.medical.exceptions.PrescriptionNotFoundException;
import com.example.medical.model.*;
import com.example.medical.repository.MedicationRepository;
import com.example.medical.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Prescription testPrescription;
    private Appointment testAppointment;
    private Medication testMedication;

    @BeforeEach
    void setUp() {
        testAppointment = Appointment.builder()
                .id(1)
                .appointmentDate(LocalDateTime.now())
                .reason("Regular checkup")
                .build();

        testPrescription = Prescription.builder()
                .id(1)
                .appointment(testAppointment)
                .issuedDate(LocalDateTime.now())
                .instructions("Take with food")
                .medications(new ArrayList<>())
                .build();

        testMedication = Medication.builder()
                .id(1)
                .name("Aspirin")
                .description("Pain reliever")
                .build();
    }

    @Test
    @DisplayName("Create prescription successfully")
    void create_ShouldSaveAndReturnPrescription() {
        // Given
        when(appointmentService.getById(1)).thenReturn(testAppointment);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(testPrescription);

        // When
        Prescription result = prescriptionService.create(1, "Take with food");

        // Then
        assertNotNull(result);
        assertEquals(testPrescription.getId(), result.getId());
        assertEquals(testPrescription.getInstructions(), result.getInstructions());
        verify(appointmentService, times(1)).getById(1);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    @DisplayName("Create prescription with null instructions should work")
    void create_WithNullInstructions_ShouldWork() {
        // Given
        when(appointmentService.getById(1)).thenReturn(testAppointment);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(testPrescription);

        // When
        Prescription result = prescriptionService.create(1, null);

        // Then
        assertNotNull(result);
        verify(appointmentService, times(1)).getById(1);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    @DisplayName("Get prescription by ID successfully")
    void getById_ShouldReturnPrescription() {
        // Given
        when(prescriptionRepository.findById(1)).thenReturn(Optional.of(testPrescription));

        // When
        Prescription result = prescriptionService.getById(1);

        // Then
        assertNotNull(result);
        assertEquals(testPrescription.getId(), result.getId());
        verify(prescriptionRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Get prescription by ID not found should throw exception")
    void getById_NotFound_ShouldThrowException() {
        // Given
        when(prescriptionRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.getById(999));
        verify(prescriptionRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Add medication to prescription successfully")
    void addMedication_ShouldAddAndReturnPrescription() {
        // Given
        when(prescriptionRepository.findById(1)).thenReturn(Optional.of(testPrescription));
        when(medicationRepository.findById(1)).thenReturn(Optional.of(testMedication));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(testPrescription);

        // When
        Prescription result = prescriptionService.addMedication(1, 1, "2 pills daily");

        // Then
        assertNotNull(result);
        verify(prescriptionRepository, times(1)).findById(1);
        verify(medicationRepository, times(1)).findById(1);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    @DisplayName("Add medication to non-existent prescription should throw exception")
    void addMedication_PrescriptionNotFound_ShouldThrowException() {
        // Given
        when(prescriptionRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PrescriptionNotFoundException.class,
                () -> prescriptionService.addMedication(999, 1, "2 pills daily"));
        verify(prescriptionRepository, times(1)).findById(999);
        verify(medicationRepository, never()).findById(any());
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Add non-existent medication to prescription should throw exception")
    void addMedication_MedicationNotFound_ShouldThrowException() {
        // Given
        when(prescriptionRepository.findById(1)).thenReturn(Optional.of(testPrescription));
        when(medicationRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MedicationNotFoundException.class,
                () -> prescriptionService.addMedication(1, 999, "2 pills daily"));
        verify(prescriptionRepository, times(1)).findById(1);
        verify(medicationRepository, times(1)).findById(999);
        verify(prescriptionRepository, never()).save(any());
    }
}

