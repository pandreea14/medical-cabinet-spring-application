package com.example.medical.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {
    private Integer id;

    @NotNull(message = "Patient is required")
    private PatientDto patient;

    @NotNull(message = "Doctor is required")
    private DoctorDto doctor;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Reason for appointment is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    @JsonIgnore
    private List<PrescriptionDto> prescriptions = new ArrayList<>();
}

