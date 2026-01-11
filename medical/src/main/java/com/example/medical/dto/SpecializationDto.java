package com.example.medical.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecializationDto {
    private Integer id;

    @NotBlank(message = "Specialization name is required")
    @Size(min = 2, max = 100, message = "Specialization name must be between 2 and 100 characters")
    private String name;
    
    @JsonIgnore
    private List<DoctorDto> doctors = new ArrayList<>();
}

