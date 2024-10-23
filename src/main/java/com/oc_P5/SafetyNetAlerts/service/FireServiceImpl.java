package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonByAddress;
import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
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
        if(!firestationRepository.existsByAddress(address)) {
            throw new NotFoundException("There is no Firestation at this address = " + address);
        }

        Optional<Person> personOptional = personRepository.findByAddress(address);
        if (personOptional.isEmpty()) {
            throw new NotFoundException("There is no Person at this address = " + address);
        }

        Integer stationNumber = firestationRepository.getAll()
                .stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .map(Firestation::getStation)
                .findFirst()
                .orElseThrow();

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