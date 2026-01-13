package com.example.medical.service;

import com.example.medical.exceptions.PatientNotFroundException;
import com.example.medical.model.Patient;
import com.example.medical.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .appointments(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Create patient - should save and return patient successfully")
    void create_ShouldSaveAndReturnPatient() {
        // Given
        Patient newPatient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient result = patientService.create(newPatient);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(patientRepository, times(1)).save(newPatient);
    }

    @Test
    @DisplayName("Create patient with null - should throw IllegalArgumentException")
    void create_WithNull_ShouldThrowIllegalArgumentException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.create(null)
        );

        assertEquals("Patient cannot be null", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get all patients - should return list of all patients")
    void getAll_ShouldReturnAllPatients() {
        // Given
        Patient patient2 = Patient.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Smith")
                .cnp("9876543210987")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .build();

        List<Patient> patients = Arrays.asList(testPatient, patient2);
        when(patientRepository.findAll()).thenReturn(patients);

        // When
        List<Patient> result = patientService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get all patients - should return empty list when no patients exist")
    void getAll_ShouldReturnEmptyList_WhenNoPatientsExist() {
        // Given
        when(patientRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<Patient> result = patientService.getAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get patient by ID - should return patient when found")
    void getById_ShouldReturnPatient_WhenPatientExists() {
        // Given
        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));

        // When
        Patient result = patientService.getById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(patientRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Get patient by ID - should throw PatientNotFroundException when not found")
    void getById_ShouldThrowPatientNotFoundException_WhenPatientNotFound() {
        // Given
        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PatientNotFroundException.class, () -> patientService.getById(999));
        verify(patientRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Update patient - should update and return patient successfully")
    void update_ShouldUpdateAndReturnPatient() {
        // Given
        Patient updatedDetails = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .cnp("9876543210987")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .build();

        Patient updatedPatient = Patient.builder()
                .id(1)
                .firstName("Jane")
                .lastName("Smith")
                .cnp("9876543210987")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .build();

        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        // When
        Patient result = patientService.update(1, updatedDetails);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jane.smith@example.com", result.getEmail());
        verify(patientRepository, times(1)).findById(1);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Update patient - should throw exception when patient not found")
    void update_ShouldThrowPatientNotFoundException_WhenPatientNotFound() {
        // Given
        Patient updatedDetails = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PatientNotFroundException.class, () -> patientService.update(999, updatedDetails));
        verify(patientRepository, times(1)).findById(999);
        verify(patientRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update patient - should update only provided fields")
    void update_ShouldUpdateOnlyProvidedFields() {
        // Given
        Patient updatedDetails = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .cnp("1234567890123")
                .email("new.email@example.com")
                .phone("1234567890")
                .build();

        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.update(1, updatedDetails);

        // Then
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("new.email@example.com", result.getEmail());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Delete patient - should delete patient successfully")
    void delete_ShouldDeletePatient() {
        // Given
        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));
        doNothing().when(patientRepository).delete(testPatient);

        // When
        patientService.delete(1);

        // Then
        verify(patientRepository, times(1)).findById(1);
        verify(patientRepository, times(1)).delete(testPatient);
    }

    @Test
    @DisplayName("Delete patient - should throw exception when patient not found")
    void delete_ShouldThrowPatientNotFoundException_WhenPatientNotFound() {
        // Given
        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PatientNotFroundException.class, () -> patientService.delete(999));
        verify(patientRepository, times(1)).findById(999);
        verify(patientRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Create patient - should handle patient with all fields")
    void create_ShouldHandlePatientWithAllFields() {
        // Given
        Patient fullPatient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .appointments(new ArrayList<>())
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient result = patientService.create(fullPatient);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("1234567890123", result.getCnp());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhone());
        verify(patientRepository, times(1)).save(fullPatient);
    }
}

