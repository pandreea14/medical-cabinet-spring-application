package com.example.medical.service;

import com.example.medical.exceptions.SpecializationNotFoundException;
import com.example.medical.model.Specialization;
import com.example.medical.repository.SpecializationRepository;
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
class SpecializationServiceTest {

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private SpecializationService specializationService;

    private Specialization testSpecialization;

    @BeforeEach
    void setUp() {
        testSpecialization = new Specialization();
        testSpecialization.setId(1);
        testSpecialization.setName("Cardiology");
    }

    @Test
    @DisplayName("Create specialization successfully")
    void create_ShouldSaveAndReturnSpecialization() {
        // Given
        when(specializationRepository.save(any(Specialization.class))).thenReturn(testSpecialization);

        // When
        Specialization result = specializationService.create(testSpecialization);

        // Then
        assertNotNull(result);
        assertEquals(testSpecialization.getId(), result.getId());
        assertEquals(testSpecialization.getName(), result.getName());
        verify(specializationRepository, times(1)).save(testSpecialization);
    }

    @Test
    @DisplayName("Create specialization with null should throw IllegalArgumentException")
    void create_WithNull_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> specializationService.create(null));
        verify(specializationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get all specializations successfully")
    void getAll_ShouldReturnAllSpecializations() {
        // Given
        List<Specialization> specializations = Arrays.asList(testSpecialization);
        when(specializationRepository.findAll()).thenReturn(specializations);

        // When
        List<Specialization> result = specializationService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSpecialization.getId(), result.get(0).getId());
        verify(specializationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get specialization by ID successfully")
    void getById_ShouldReturnSpecialization() {
        // Given
        when(specializationRepository.findById(1)).thenReturn(Optional.of(testSpecialization));

        // When
        Specialization result = specializationService.getById(1);

        // Then
        assertNotNull(result);
        assertEquals(testSpecialization.getId(), result.getId());
        assertEquals(testSpecialization.getName(), result.getName());
        verify(specializationRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Get specialization by ID not found should throw exception")
    void getById_NotFound_ShouldThrowException() {
        // Given
        when(specializationRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(SpecializationNotFoundException.class, () -> specializationService.getById(999));
        verify(specializationRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Delete specialization successfully")
    void delete_ShouldDeleteSpecialization() {
        // Given
        when(specializationRepository.findById(1)).thenReturn(Optional.of(testSpecialization));
        doNothing().when(specializationRepository).delete(testSpecialization);

        // When
        specializationService.delete(1);

        // Then
        verify(specializationRepository, times(1)).findById(1);
        verify(specializationRepository, times(1)).delete(testSpecialization);
    }

    @Test
    @DisplayName("Delete non-existent specialization should throw exception")
    void delete_NotFound_ShouldThrowException() {
        // Given
        when(specializationRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(SpecializationNotFoundException.class, () -> specializationService.delete(999));
        verify(specializationRepository, times(1)).findById(999);
        verify(specializationRepository, never()).delete(any());
    }
}

