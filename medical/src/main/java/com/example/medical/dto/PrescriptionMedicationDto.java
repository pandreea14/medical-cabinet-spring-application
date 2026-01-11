package com.example.medical.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionMedicationDto {
    private MedicationDto medication;

    @NotBlank(message = "Dosage is required")
    @Size(max = 50, message = "Dosage cannot exceed 50 characters")
    private String dosage;

    @JsonIgnore
    private PrescriptionDto prescription;
}

