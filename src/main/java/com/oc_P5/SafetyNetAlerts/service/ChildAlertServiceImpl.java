package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.NamedModel;
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
        List<Person> personList = personRepository.getByAddress(address);

        for (Person person : personList) {
            System.out.println("person = " + person.getId());
        }

        // Récupère la liste de l'id des personnes à l'adresse demandée
        List<String> idList = personList
                .stream()
                .map(NamedModel::getId)
                .toList();

        for(String id : idList) {
            System.out.println("id = " + id);
        }

        // Récupérer la liste des dossiers médicaux des Person à l'adresse demandée
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.getAll()
                .stream()
                .filter(medicalRecord -> idList.contains(medicalRecord.getId()))
                .toList();

        for(MedicalRecord medicalRecord : medicalRecordList) {
            System.out.println("medicalRecord = " + medicalRecord.getId());
        }

        // Récupère la liste accumulée de Person + MedicalRecord à l'adresse demandée
        List<PersonWithMedicalRecord> personWithMedicalRecord = personList
                .stream()
                .map(person -> mapToPersonWithMedicalRecord(person, medicalRecordList))
                .toList();

        for(PersonWithMedicalRecord personMedic : personWithMedicalRecord) {
            System.out.println("person = " + personMedic.person.getId() + " == medicalRecord = " + personMedic.medicalRecord.getId() + " == age = " + personMedic.medicalRecord.getAge());
        }

        return personWithMedicalRecord
                .stream()
                .filter(PersonWithMedicalRecord::isMinor)
                .map(personMedic -> mapToChildrenByAddress(personMedic, personWithMedicalRecord))
                .toList();
    }

    private static PersonWithMedicalRecord mapToPersonWithMedicalRecord(Person person, List<MedicalRecord> medicalRecordList) {
        return medicalRecordList
                .stream()
                .filter(medicalRecord -> medicalRecord.getId().equals(person.getId()))
                .map(medicalRecord -> new PersonWithMedicalRecord(person, medicalRecord))
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
