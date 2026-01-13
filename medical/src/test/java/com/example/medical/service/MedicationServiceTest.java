package com.example.medical.service;

import com.example.medical.exceptions.MedicationNotFoundException;
import com.example.medical.model.Medication;
import com.example.medical.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    private Medication testMedication;

    @BeforeEach
    void setUp() {
        testMedication = Medication.builder()
                .id(1)
                .name("Aspirin")
                .description("Pain reliever")
                .build();
    }

    @Test
    @DisplayName("Create medication successfully")
    void create_ShouldSaveAndReturnMedication() {
        // Given
        when(medicationRepository.save(any(Medication.class))).thenReturn(testMedication);

        // When
        Medication result = medicationService.create(testMedication);

        // Then
        assertNotNull(result);
        assertEquals(testMedication.getId(), result.getId());
        assertEquals(testMedication.getName(), result.getName());
        verify(medicationRepository, times(1)).save(testMedication);
    }

    @Test
    @DisplayName("Create medication with null should throw IllegalArgumentException")
    void create_WithNull_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> medicationService.create(null));
        verify(medicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get all medications successfully")
    void getAll_ShouldReturnAllMedications() {
        // Given
        List<Medication> medications = Arrays.asList(testMedication);
        when(medicationRepository.findAll()).thenReturn(medications);

        // When
        List<Medication> result = medicationService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMedication.getId(), result.get(0).getId());
        verify(medicationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Delete medication successfully")
    void delete_ShouldDeleteMedication() {
        when(medicationRepository.findById(1)).thenReturn(Optional.of(testMedication));
        doNothing().when(medicationRepository).delete(testMedication);

        medicationService.delete(1);

        verify(medicationRepository, times(1)).findById(1);
        verify(medicationRepository, times(1)).delete(testMedication);
    }

    @Test
    @DisplayName("Delete non-existent medication should throw exception")
    void delete_NotFound_ShouldThrowException() {
        when(medicationRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(MedicationNotFoundException.class, () -> medicationService.delete(999));
        verify(medicationRepository, times(1)).findById(999);
        verify(medicationRepository, never()).delete(any());
    }
}

