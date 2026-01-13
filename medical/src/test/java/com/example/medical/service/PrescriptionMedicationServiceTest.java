package com.example.medical.service;

import com.example.medical.model.Appointment;
import com.example.medical.model.Medication;
import com.example.medical.model.Prescription;
import com.example.medical.model.PrescriptionMedication;
import com.example.medical.repository.PrescriptionMedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionMedicationServiceTest {

    @Mock
    private PrescriptionMedicationRepository prescriptionMedicationRepository;

    @InjectMocks
    private PrescriptionMedicationService prescriptionMedicationService;

    private PrescriptionMedication testPrescriptionMedication;
    private Prescription testPrescription;
    private Medication testMedication;
    private Appointment testAppointment;

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
        testPrescriptionMedication = new PrescriptionMedication();
        testPrescriptionMedication.setId(1);
        testPrescriptionMedication.setPrescription(testPrescription);
        testPrescriptionMedication.setMedication(testMedication);
        testPrescriptionMedication.setDosage("2 tablets daily");
    }

    @Test
    void updateDosage_WhenPrescriptionMedicationExists_ShouldUpdateDosage() {
        when(prescriptionMedicationRepository.findById(1)).thenReturn(Optional.of(testPrescriptionMedication));
        when(prescriptionMedicationRepository.save(any(PrescriptionMedication.class)))
                .thenReturn(testPrescriptionMedication);

        prescriptionMedicationService.updateDosage(1, "3 tablets daily");

        verify(prescriptionMedicationRepository, times(1)).findById(1);
        verify(prescriptionMedicationRepository, times(1)).save(testPrescriptionMedication);
        assertThat(testPrescriptionMedication.getDosage()).isEqualTo("3 tablets daily");
    }

    @Test
    void updateDosage_WhenPrescriptionMedicationDoesNotExist_ShouldThrowException() {
        when(prescriptionMedicationRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> prescriptionMedicationService.updateDosage(999, "New dosage"))
                .isInstanceOf(RuntimeException.class);

        verify(prescriptionMedicationRepository, times(1)).findById(999);
        verify(prescriptionMedicationRepository, never()).save(any(PrescriptionMedication.class));
    }

    @Test
    void delete_WhenPrescriptionMedicationExists_ShouldDeletePrescriptionMedication() {
        doNothing().when(prescriptionMedicationRepository).deleteById(1);

        prescriptionMedicationService.delete(1);

        verify(prescriptionMedicationRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_ShouldCallDeleteById() {
        doNothing().when(prescriptionMedicationRepository).deleteById(999);

        prescriptionMedicationService.delete(999);

        verify(prescriptionMedicationRepository, times(1)).deleteById(999);
    }
}

