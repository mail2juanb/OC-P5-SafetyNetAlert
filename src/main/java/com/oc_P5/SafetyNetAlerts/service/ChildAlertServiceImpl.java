package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.NamedModel;
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
public class ChildAlertServiceImpl implements ChildAlertService {

    private final PersonRepository personRepository;


    public List<ChildrenByAddress> getChildByAddress(String address) {
        // NOTE Vérifier si l'adresse est null ou vide
        if (address == null || StringUtils.isBlank(address)) {
            throw new NullOrEmptyObjectException("Address cannot be null or empty");
        }

        // NOTE Pas de vérification de l'existence de personnes à l'adresse demandée puisque la liste retournée peut être vide.

        // NOTE Récupère la liste de l'id des personnes à l'adresse demandée
        List<String> idList = personRepository.getByAddress(address)
                .stream()
                .map(NamedModel::getId)
                .toList();

        // NOTE Récupère la liste des PersonWithMedicalRecord avec la idList
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
