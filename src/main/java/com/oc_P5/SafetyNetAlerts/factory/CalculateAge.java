package com.oc_P5.SafetyNetAlerts.factory;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CalculateAge {

    // Calcul le nombre d'années entre maintenant et une String de date d'anniversaire envoyée.
    public int calculateAge(String strBirthDate) {
        LocalDate personBirthDate = LocalDate.parse(strBirthDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return Period.between(personBirthDate, LocalDate.now()).getYears();
    }
}
