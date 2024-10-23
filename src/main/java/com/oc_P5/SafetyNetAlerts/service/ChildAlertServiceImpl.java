package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.model.NamedModel;
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
public class ChildAlertServiceImpl implements ChildAlertService {

    private final PersonRepository personRepository;

    @Override
    public List<ChildrenByAddress> getChildByAddress(String address) {
        // NOTE : No verification of the existence of people at the requested address, as the list returned may be empty.

        List<String> idList = personRepository.getByAddress(address)
                .stream()
                .map(NamedModel::getId)
                .toList();

        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(idList);

        return personWithMedicalRecordList
                .stream()
                .filter(PersonWithMedicalRecord::isMinor)
                .map(personMedic -> mapToChildrenByAddress(personMedic, personWithMedicalRecordList))
                .toList();
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

}
