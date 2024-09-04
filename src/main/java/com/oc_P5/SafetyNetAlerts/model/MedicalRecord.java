package com.oc_P5.SafetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
public class MedicalRecord extends NamedModel {

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;
    private List<String> medications;
    private List<String> allergies;


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
