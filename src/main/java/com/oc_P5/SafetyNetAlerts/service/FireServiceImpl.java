package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonByAddress;
import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.NamedModel;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FireServiceImpl implements FireService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    @Override
    public FirePersonsResponse getFirePersonsByAddress(String address) {

        Integer stationNumber = firestationRepository.getAll()
                .stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .map(Firestation::getStation)
                .findFirst()
                .orElse(null);

        List<Person> personsByAddress = personRepository.getByAddress(address);

        List<String> personIds = personsByAddress
                .stream()
                .map(NamedModel::getId)
                .toList();

        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(personIds);

        List<FirePersonByAddress> firePersonByAdressList = personWithMedicalRecordList
                .stream()
                .map(FireServiceImpl::mapToFirePersonByAddress)
                .toList();

        return new FirePersonsResponse (firePersonByAdressList, address, stationNumber);
    }

    private static FirePersonByAddress mapToFirePersonByAddress(PersonWithMedicalRecord person) {
        return new FirePersonByAddress(person.person(), person.medicalRecord());
    }

}