package com.example.medical.service;

import com.example.medical.exceptions.DoctorNotFoundException;
import com.example.medical.exceptions.SpecializationNotFoundException;
import com.example.medical.model.Doctor;
import com.example.medical.model.Specialization;
import com.example.medical.repository.DoctorRepository;
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
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor testDoctor;
    private Specialization testSpecialization;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("Create doctor successfully")
    void create_ShouldSaveAndReturnDoctor() {
        when(specializationRepository.findByName("Cardiology")).thenReturn(Optional.of(testSpecialization));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        Doctor result = doctorService.create(testDoctor);

        assertNotNull(result);
        assertEquals(testDoctor.getId(), result.getId());
        assertEquals(testDoctor.getFirstName(), result.getFirstName());
        verify(specializationRepository, times(1)).findByName("Cardiology");
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Create doctor with null should throw IllegalArgumentException")
    void create_WithNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> doctorService.create(null));
        verify(doctorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create doctor with non-existent specialization should throw SpecializationNotFoundException")
    void create_WithNonExistentSpecialization_ShouldThrowException() {
        when(specializationRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        Specialization nonExistent = new Specialization();
        nonExistent.setName("NonExistent");

        Doctor doctor = Doctor.builder()
                .firstName("Dr. Jane")
                .email("test@example.com")
                .specialization(nonExistent)
                .build();

        assertThrows(SpecializationNotFoundException.class, () -> doctorService.create(doctor));
        verify(doctorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get all doctors successfully")
    void getAll_ShouldReturnAllDoctors() {
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> result = doctorService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDoctor.getId(), result.get(0).getId());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get doctor by ID successfully")
    void getById_ShouldReturnDoctor() {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));

        Doctor result = doctorService.getById(1);

        assertNotNull(result);
        assertEquals(testDoctor.getId(), result.getId());
        assertEquals(testDoctor.getFirstName(), result.getFirstName());
        verify(doctorRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Get doctor by ID not found should throw exception")
    void getById_NotFound_ShouldThrowException() {
        when(doctorRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.getById(999));
        verify(doctorRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Get doctors by specialization successfully")
    void getBySpecialization_ShouldReturnDoctors() {
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorRepository.findBySpecializationId(1)).thenReturn(doctors);

        List<Doctor> result = doctorService.getBySpecialization(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(doctorRepository, times(1)).findBySpecializationId(1);
    }

    @Test
    @DisplayName("Update doctor successfully")
    void update_ShouldUpdateAndReturnDoctor() {
        Doctor updatedDetails = Doctor.builder()
                .firstName("Dr. John")
                .lastName("Updated")
                .email("john.updated@example.com")
                .phone("1234567890")
                .specialization(testSpecialization)
                .build();

        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));
        when(specializationRepository.findByName("Cardiology")).thenReturn(Optional.of(testSpecialization));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        Doctor result = doctorService.update(1, updatedDetails);

        assertNotNull(result);
        verify(doctorRepository, times(1)).findById(1);
        verify(specializationRepository, times(1)).findByName("Cardiology");
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Update doctor with non-existent specialization should throw exception")
    void update_WithNonExistentSpecialization_ShouldThrowException() {
        Specialization nonExistent = new Specialization();
        nonExistent.setName("NonExistent");

        Doctor updatedDetails = Doctor.builder()
                .firstName("Dr. John")
                .specialization(nonExistent)
                .build();

        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));
        when(specializationRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(SpecializationNotFoundException.class, () -> doctorService.update(1, updatedDetails));
        verify(doctorRepository, times(1)).findById(1);
        verify(doctorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete doctor successfully")
    void delete_ShouldDeleteDoctor() {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));
        doNothing().when(doctorRepository).delete(testDoctor);

        doctorService.delete(1);

        verify(doctorRepository, times(1)).findById(1);
        verify(doctorRepository, times(1)).delete(testDoctor);
    }

    @Test
    @DisplayName("Delete non-existent doctor should throw exception")
    void delete_NotFound_ShouldThrowException() {
        when(doctorRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.delete(999));
        verify(doctorRepository, times(1)).findById(999);
        verify(doctorRepository, never()).delete(any());
    }
}

