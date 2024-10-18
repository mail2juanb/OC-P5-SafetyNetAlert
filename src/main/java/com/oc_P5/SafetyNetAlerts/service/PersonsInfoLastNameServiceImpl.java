package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonsInfoLastNameServiceImpl implements PersonsInfoLastNameService {

    private final PersonRepository personRepository;


    @Override
    public List<PersonInfoLastName> getPersonsInfoLastName(String lastName) {
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

        // NOTE Recupère la liste des PersonWithMedicalRecord
        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(idsList);

        // NOTE Mapper la liste de PersonWithMedicalRecord dans une liste de PersonInfoLastName
        return personWithMedicalRecordList
                .stream()
                .map(PersonsInfoLastNameServiceImpl::mapToPersonsByLastName)
                .toList();
    }

    private static PersonInfoLastName mapToPersonsByLastName(PersonWithMedicalRecord personMedic) {
        return new PersonInfoLastName(personMedic.person(), personMedic.medicalRecord());
    }

}
