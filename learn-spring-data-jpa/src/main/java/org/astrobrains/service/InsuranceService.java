package org.astrobrains.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.astrobrains.model.Insurance;
import org.astrobrains.model.Patient;
import org.astrobrains.repository.InsuranceRepository;
import org.astrobrains.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final PatientRepository patientRepository;
    private final InsuranceRepository insuranceRepository;

    @Transactional
    public Patient saveInsuranceToPatient(Long patientId, Insurance insurance) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new RuntimeException("Patient not found with id" + patientId));

        patient.setInsurance(insurance);
        insurance.setPatient(patient); // bidirectional relationship (not required)

        return null;
    }
}
