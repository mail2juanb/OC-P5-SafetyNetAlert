package com.oc_P5.SafetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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


    public MedicalRecord update(MedicalRecord update){

        if(update.getBirthdate() != null) {
            setBirthdate(update.getBirthdate());
        }

        if(update.getMedications() != null) {
            setMedications(update.getMedications());
        }

        if(update.getAllergies() != null) {
            setAllergies(update.getAllergies());
        }

        return this;
    }
}
