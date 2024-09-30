package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;

    public List<String> getPhonesByStation(Integer stationNumber) {
        if(stationNumber == null)
            throw new NullOrEmptyObjectException("Station can not be null");

        if(!firestationRepository.existsByStation(stationNumber)) {
            throw new NotFoundException("station " + stationNumber + " does not exists");
        }

        List<String> firestationsAddresses = firestationRepository.getByStation(stationNumber)
                .stream()
                .map(Firestation::getAddress)
                .toList();

        List<Person> personsByAddress = personRepository.getByAddresses(firestationsAddresses);

        return personsByAddress
                .stream()
                .filter(person -> person.getPhone() != null && !person.getPhone().isEmpty())
                .map(Person::getPhone)
                .toList();
    }

}
