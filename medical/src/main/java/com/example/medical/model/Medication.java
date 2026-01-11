package com.example.medical.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "medication")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL)
    private List<PrescriptionMedication> prescriptionMedications = new ArrayList<>();

    public Medication() {
    }

    public Medication(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
