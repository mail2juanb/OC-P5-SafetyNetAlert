package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonsInfoLastNameServiceImpl implements PersonsInfoLastNameService {

    private final PersonRepository personRepository;

    public List<PersonInfoLastName> getPersonsInfoLastName(String lastName) {

        // NOTE Vérifier si le lastName est null ou vide
        if(StringUtils.isBlank(lastName)) {
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

        // NOTE Recupère la liste des PersonWithMedicalRecord
        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(idsList);

        // NOTE Mapper la liste de PersonWithMedicalRecord dans une liste de PersonInfoLastName
        return personWithMedicalRecordList
                .stream()
                .map(PersonsInfoLastNameServiceImpl::mapToPersonsBylastName)
                .toList();
    }

    private static PersonInfoLastName mapToPersonsBylastName(PersonWithMedicalRecord personMedic) {
        return new PersonInfoLastName(personMedic.person(), personMedic.medicalRecord());
    }

}
