package com.oc_P5.SafetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord extends NamedModel {

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;
    private List<String> medications;
    private List<String> allergies;

    // FIXME Constructeur avec tous les champs mais pourquoi ? L'annotation lombok ne fonctionne pas ?
    public MedicalRecord(String firstName, String lastName, LocalDate birthdate, List<String> medications, List<String> allergies) {
        super(firstName, lastName);
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }


    public Integer getAge(){
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    public boolean isMinor(){
        return getAge() <= 18;
    }

    public boolean isMajor(){
        return !isMinor();
    }
}
