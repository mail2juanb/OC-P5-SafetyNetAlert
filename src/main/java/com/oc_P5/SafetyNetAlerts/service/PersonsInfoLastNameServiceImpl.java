package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
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

    private final MedicalRecordRepository medicalRecordRepository;


    @Override
    public List<PersonInfoLastName> getPersonsInfoLastName(String lastName) {
        if(!personRepository.existsByLastName(lastName)) {
            throw new NotFoundException("Person doesn't exist with lastName = " + lastName);
        }

        if(!medicalRecordRepository.existsByLastName(lastName)) {
            throw new NotFoundException("MedicalRecord doesn't exist with lastName = " + lastName);
        }

        List<String> idsList = personRepository.getAll()
                .stream()
                .filter(person -> person.getLastName().equals(lastName))
                .map(Person::getId)
                .toList();

        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(idsList);

        return personWithMedicalRecordList
                .stream()
                .map(PersonsInfoLastNameServiceImpl::mapToPersonsByLastName)
                .toList();
    }

    private static PersonInfoLastName mapToPersonsByLastName(PersonWithMedicalRecord personMedic) {
        return new PersonInfoLastName(personMedic.person(), personMedic.medicalRecord());
    }

}
