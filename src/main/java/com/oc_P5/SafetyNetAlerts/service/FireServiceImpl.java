package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonByAddress;
import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.*;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

        // NOTE Vérifier si l'adresse est null ou vide
        if (StringUtils.isBlank(address)) {
            throw new NullOrEmptyObjectException("Address cannot be null or empty");
        }

        // NOTE Vérifier que l'adresse existe dans la liste des Firestation
        if(!firestationRepository.existsByAddress(address)) {
            throw new NotFoundException("There is no Firestation at this address = " + address);
        }

        // NOTE Vérifier que l'adresse existe dans la liste des Person
        Optional<Person> personOptional = personRepository.findByAddress(address);
        if (personOptional.isEmpty()) {
            throw new NotFoundException("There is no Person at this address = " + address);
        }

        // NOTE Récupère la stationNumber correspondant à l'adresse
        Integer stationNumber = firestationRepository.getAll()
                .stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .map(Firestation::getStation)
                .findFirst()
                .orElseThrow();

        // NOTE Récupére la liste des personnes correspondant à l'adresse
        List<Person> personsByAddress = personRepository.getByAddress(address);

        // NOTE Récupère la liste de l'id des personnes à l'adresse demandée
        List<String> personIds = personsByAddress
                .stream()
                .map(NamedModel::getId)
                .toList();

        // NOTE Récupère la liste de PersonWithMedicalRecord avec les personIds
        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(personIds);

        // NOTE Mapper la liste de PersonWithMedicalRecord dans une liste de FirePersonByAddress
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