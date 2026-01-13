package com.example.medical.service;

import com.example.medical.exceptions.SpecializationNotFoundException;
import com.example.medical.model.Specialization;
import com.example.medical.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public Specialization create(Specialization specialization) {
        if (specialization == null) {
            throw new IllegalArgumentException("Specialization cannot be null");
        }
        return specializationRepository.save(specialization);
    }

    public List<Specialization> getAll() {
        return specializationRepository.findAll();
    }

    public Specialization getById(Integer id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new SpecializationNotFoundException(id));
    }

    public void delete(Integer id) {
        specializationRepository.delete(getById(id));
    }
}
