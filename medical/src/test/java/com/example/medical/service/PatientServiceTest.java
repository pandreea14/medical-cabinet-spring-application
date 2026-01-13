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
        Patient newPatient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        Patient result = patientService.create(newPatient);

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

        List<Patient> result = patientService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get all patients - should return empty list when no patients exist")
    void getAll_ShouldReturnEmptyList_WhenNoPatientsExist() {
        when(patientRepository.findAll()).thenReturn(new ArrayList<>());

        List<Patient> result = patientService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get patient by ID - should return patient when found")
    void getById_ShouldReturnPatient_WhenPatientExists() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));

        Patient result = patientService.getById(1);

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
        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(PatientNotFroundException.class, () -> patientService.getById(999));
        verify(patientRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Update patient - should update and return patient successfully")
    void update_ShouldUpdateAndReturnPatient() {
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

        Patient result = patientService.update(1, updatedDetails);

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
        Patient updatedDetails = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(PatientNotFroundException.class, () -> patientService.update(999, updatedDetails));
        verify(patientRepository, times(1)).findById(999);
        verify(patientRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update patient - should update only provided fields")
    void update_ShouldUpdateOnlyProvidedFields() {
        Patient updatedDetails = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .cnp("1234567890123")
                .email("new.email@example.com")
                .phone("1234567890")
                .build();

        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.update(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("new.email@example.com", result.getEmail());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Delete patient - should delete patient successfully")
    void delete_ShouldDeletePatient() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));
        doNothing().when(patientRepository).delete(testPatient);

        patientService.delete(1);

        verify(patientRepository, times(1)).findById(1);
        verify(patientRepository, times(1)).delete(testPatient);
    }

    @Test
    @DisplayName("Delete patient - should throw exception when patient not found")
    void delete_ShouldThrowPatientNotFoundException_WhenPatientNotFound() {
        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(PatientNotFroundException.class, () -> patientService.delete(999));
        verify(patientRepository, times(1)).findById(999);
        verify(patientRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Create patient - should handle patient with all fields")
    void create_ShouldHandlePatientWithAllFields() {
        Patient fullPatient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .cnp("1234567890123")
                .email("john.doe@example.com")
                .phone("1234567890")
                .appointments(new ArrayList<>())
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        Patient result = patientService.create(fullPatient);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("1234567890123", result.getCnp());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhone());
        verify(patientRepository, times(1)).save(fullPatient);
    }
}

