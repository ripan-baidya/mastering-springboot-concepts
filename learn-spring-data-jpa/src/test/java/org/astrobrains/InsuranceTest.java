package org.astrobrains;

import org.astrobrains.model.Insurance;
import org.astrobrains.model.Patient;
import org.astrobrains.service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class InsuranceTest {

    @Autowired
    private InsuranceService insuranceService;

    @Test
    public void addInsuranceToPatient() {
        Insurance insurance = Insurance.builder()
                .policyNumber("SBI-432")
                .provider("SBI")
                .validUntil(LocalDate.of(2030, 12, 12))
                .build();
        Patient patient = insuranceService.saveInsuranceToPatient(1L, insurance);

        System.out.println(patient);
    }
}
