package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;

import java.util.List;
import java.util.stream.Collectors;


public class ChildrenService_A_Supprimer {

    // Méthode pour récupérer la liste des enfants par adresse
    public List<ChildrenByAddress> getChildrenByAddress(List<Person> persons, List<MedicalRecord> medicalRecords, String address) {
        // Filtrer les personnes qui vivent à l'adresse donnée
        List<Person> personsAtAddress = persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();

        // Retourner une liste de ChildrenByAddress pour les enfants mineurs
        return personsAtAddress.stream()
                .map(person -> {
                    // Récupérer le dossier médical correspondant
                    MedicalRecord medicalRecord = medicalRecords.stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                                    record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    if (medicalRecord != null && medicalRecord.isMinor()) {
                        // Récupérer l'âge de l'enfant
                        int age = medicalRecord.getAge();

                        // Récupérer les membres de la famille (autres personnes vivant à la même adresse)
                        List<Person> familyMembers = personsAtAddress.stream()
                                .filter(p -> !p.equals(person)) // Exclure l'enfant lui-même
                                .collect(Collectors.toList());

                        // Créer et retourner un objet ChildrenByAddress
                        return new ChildrenByAddress(person, age, familyMembers);
                    }
                    return null; // Retourner null si la personne n'est pas un enfant mineur
                })
                .filter(childrenByAddress -> childrenByAddress != null) // Filtrer les objets null
                .collect(Collectors.toList());
    }
}
