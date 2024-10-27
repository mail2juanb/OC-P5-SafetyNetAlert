package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonByAddress;
import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.model.*;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class FireServiceImpl implements FireService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    @Override
    public FirePersonsResponse getFirePersonsByAddress(String address) {

        Optional<Integer> stationNumber = firestationRepository.getAll()
                .stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .map(Firestation::getStation)
                .findFirst();

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