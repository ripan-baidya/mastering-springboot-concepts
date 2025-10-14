package org.astrobrains.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.astrobrains.dto.PatientRequest;
import org.astrobrains.model.Patient;
import org.astrobrains.repository.PatientRepository;
import org.astrobrains.service.PatientService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public Patient savePatient(PatientRequest request) {
        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .birthDate(request.getBrithDate())
                .bloodGroup(request.getBloodGroup())
                .gender(request.getGender())
                .build();
        patientRepository.save(patient);

        log.info("Patient with email {} Saved Successfully", request.getEmail());
        return patient;
    }

    @Override
    public Patient findPatientByEmail(String email) throws IllegalAccessException {
        Patient patient = patientRepository.findByEmail(email);
        if (patient == null) throw new IllegalAccessException("Patient not found with email " + email);
        return patient;
    }

}
