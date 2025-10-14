package org.astrobrains;

import org.astrobrains.model.Patient;
import org.astrobrains.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class PatientTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void testPatient() {
//        Patient patient = patientRepository.findByEmail("rohit.sharma@example.com");

//        List<Patient> patients = patientRepository.findByFullNameOrEmail("Neha Verma", "priya.singh@example.com");

//        List<Patient> patients = patientRepository.findByBloodGroup(BloodGroup.AB_POSITIVE);

//        List<Patient> patients = patientRepository.findPatientBornAfter(LocalDate.of(1950, 1, 1));

//        List<Patient> patients = patientRepository.findAllPatients();
//
//        for (Patient patient : patients) {
//            System.out.println(patient.getId() + " " + patient.getFullName());
//        }
//        List<Object[]> patients = patientRepository.countPatientByBloodGroup();
//
//        for (Object[] patient : patients) {
//            System.out.println(patient[0] + " " + patient[1]);
//        }

//        int rowsUpdated = patientRepository.updatePatientName(1L, "Ripan Baidya");
//        System.out.println(rowsUpdated);

//        int rowsDeleted = patientRepository.deletePatientById(1L);
//        System.out.println(rowsDeleted);

        // Projection using interface
//        List<PatientNameEmail> patients = patientRepository.findPatientNameEmail();
//        patients.forEach(patient -> System.out.println(patient.getFullName() + " " + patient.getEmail()));

        // Projection using dto(class)
//        List<PatientNameGender> patientNameGenders = patientRepository.findPatientNameGender();
//        patientNameGenders.forEach(patient -> System.out.println(patient.getFullName() + " " + patient.getGender()));

        Pageable pageable = PageRequest.of(0, 5, Sort.by("fullName").descending());
        Page<Patient> pages = patientRepository.findAll(pageable);
        List<Patient> patients = pages.getContent();
        patients.forEach(p -> System.out.println(p.getId() + " " + p.getFullName() + " " + p.getEmail()));
    }
}
