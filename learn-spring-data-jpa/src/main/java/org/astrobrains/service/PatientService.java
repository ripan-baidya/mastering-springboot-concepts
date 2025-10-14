package org.astrobrains.service;

import org.astrobrains.dto.PatientRequest;
import org.astrobrains.model.Patient;

public interface PatientService {

    Patient savePatient(PatientRequest request);

    Patient findPatientByEmail(String email) throws IllegalAccessException;

}
