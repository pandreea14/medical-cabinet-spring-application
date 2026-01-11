package com.example.medical.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionDto {
    private Integer id;

    @NotNull(message = "Issued date is required")
    @PastOrPresent(message = "Issued date cannot be in the future")
    private LocalDateTime issuedDate;

    @NotBlank(message = "Instructions are required")
    @Size(max = 1000, message = "Instructions cannot exceed 1000 characters")
    private String instructions;

    private List<PrescriptionMedicationDto> medications = new ArrayList<>();
}

