package org.astrobrains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.astrobrains.enums.BloodGroup;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    
    private String fullName;

    private String email;

    private LocalDate brithDate;

    private String gender;

    private BloodGroup bloodGroup;

}
