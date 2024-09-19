package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChildAlertServiceImpl implements ChildAlertService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;


    public List<ChildrenByAddress> getChildByAddress(String address) {
        // Vérifier si l'adresse est null ou vide
        if (address == null || address.trim().isEmpty()) {
            throw new NullOrEmptyObjectException("Address cannot be null or empty");
        }
        // Pas de vérification de l'existence de personnes à l'adresse demandée puisque la liste retournée peut être vide.

        // Récupérer la liste des personnes à l'adresse demandée
        List<Person> personsByAddress = personRepository.getByAddress(address);

        // Récupère la liste de l'id des personnes à l'adresse demandée
        List<String> personIds = personsByAddress
                .stream()
                .map(p -> p.getId())
                .toList();

        // Récupérer la liste des dossiers médicaux des mineurs à l'adresse demandée
        List<MedicalRecord> medicalRecordMinorsByAddress = medicalRecordRepository.getAll()
                .stream()
                .filter(medicalRecord -> personIds.contains(medicalRecord.getId()))
                .filter(MedicalRecord::isMinor)
                .toList();

        // Récupère la liste accumulée de Person + MedicalRecord à l'adresse demandée
        List<PersonWithMedicalRecord> personWithMedicalRecord = personsByAddress
                .stream()
                .map( p -> mapToPersonWithMedicalRecord(p, medicalRecordMinorsByAddress, personIds))
                .toList();

        return personWithMedicalRecord
                .stream()
                .filter(p -> p.isMinor())
                .map(p -> mapToChildrenByAddress(p, personWithMedicalRecord))
                .toList();
    }

    private static PersonWithMedicalRecord mapToPersonWithMedicalRecord(Person p, List<MedicalRecord> medicalRecordMinorsByAddress, List<String> personIds) {
        return medicalRecordMinorsByAddress
                .stream()
                .filter(mr -> personIds.contains(mr.getId()))
                .map(mr -> new PersonWithMedicalRecord(p, mr))
                .findFirst()
                .orElseThrow();
    }

    private static ChildrenByAddress mapToChildrenByAddress(PersonWithMedicalRecord person, List<PersonWithMedicalRecord> persons) {
        List<Person> familyMembers = persons
                .stream()
                .filter(p -> !p.equals(person))
                .filter(p -> p.getLastName().equals(person.getLastName()))
                .map(PersonWithMedicalRecord::person)
                .toList();

        return new ChildrenByAddress(person.person(), person.getAge(), familyMembers);
    }

    record PersonWithMedicalRecord(Person person, MedicalRecord medicalRecord){
        public String getFirstName(){
            return person.getFirstName();
        }
        public String getLastName(){
            return person.getLastName();
        }
        public Integer getAge(){
            return medicalRecord.getAge();
        }
        public boolean isMinor(){
            return medicalRecord.isMinor();
        }
    }

}
