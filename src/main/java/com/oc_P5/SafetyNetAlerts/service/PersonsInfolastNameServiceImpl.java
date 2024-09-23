package com.oc_P5.SafetyNetAlerts.service;


import com.oc_P5.SafetyNetAlerts.dto.PersonsInfolastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
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
public class PersonsInfolastNameServiceImpl implements PersonsInfolastNameService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PersonsInfolastName getPersonsInfolastName(String lastName) {

        // NOTE Vérifier si le lastName est null ou vide
        if(lastName == null || lastName.trim().isEmpty()) {
            throw new NullOrEmptyObjectException("lastName can not be null or empty");
        }

        // NOTE Vérifier si le lastName existe
        if(!personRepository.existsByLastName(lastName)) {
            throw new NotFoundException("Person doesn't exist with lastName = " + lastName);
        }

        // NOTE Récupérer la liste des ids ayant le lastName demandé
        List<String> idsList = personRepository.getAll()
                .stream()
                .filter(person -> person.getLastName().equals(lastName))
                .map(Person::getId)
                .toList();

        for(String id : idsList) {
            System.out.println("id = " + id);
        }

        // NOTE Récupérer la liste des Person correspondant aux ids demandés
        List<Person> personList = personRepository.getAll()
                .stream()
                .filter(person -> idsList.contains(person.getId()))
                .toList();

        for(Person person : personList) {
            System.out.println("Person = " + person.getId());
        }

        // NOTE Récupérer la liste des MedicalRecord correspondant aux ids demandés
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.getAll()
                .stream()
                .filter(medicalRecord -> idsList.contains(medicalRecord.getId()))
                .toList();

        for(MedicalRecord medicalRecord : medicalRecordList) {
            System.out.println("medicalRecord = " + medicalRecord.getId());
        }

        // NOTE Mapper les 2 liste en une seule dans une liste d'objets PersonWithMedicalRecord
        List<PersonWithMedicalRecord> personWithMedicalRecordList = personList
                .stream()
                .map(person -> mapToPersonWithMedicalRecord(person, medicalRecordList))
                .toList();

        for(PersonWithMedicalRecord personMedic : personWithMedicalRecordList) {
            System.out.println("Person = " + personMedic.person.getId() + " == MedialRecord = " + personMedic.medicalRecord.getId());
        }

        // NOTE Mapper la liste de PersonWithMedicalRecord dans une liste de PersonInfolastName
        List<PersonsInfolastName.PersonInfolastName> personInfolastNameList = personWithMedicalRecordList
                .stream()
                .map(PersonsInfolastNameServiceImpl::mapToPersonsBylastName)
                .toList();

        return new PersonsInfolastName(personInfolastNameList);
    }

    private static PersonsInfolastName.PersonInfolastName mapToPersonsBylastName(PersonWithMedicalRecord personMedic) {
        return new PersonsInfolastName.PersonInfolastName(personMedic.person, personMedic.medicalRecord);
    }

    private static PersonWithMedicalRecord mapToPersonWithMedicalRecord(Person person, List<MedicalRecord> medicalRecordList) {
        return medicalRecordList
                .stream()
                .filter(medicalRecord -> medicalRecord.getId().equals(person.getId()))
                .map(medicalRecord -> new PersonWithMedicalRecord(person, medicalRecord))
                .findFirst()
                .orElseThrow();
    }

    record PersonWithMedicalRecord(Person person, MedicalRecord medicalRecord){
        public String getFirstName() {
            return person.getFirstName();
        }
        public String getLastName() {
            return person.getLastName();
        }
    }

}
