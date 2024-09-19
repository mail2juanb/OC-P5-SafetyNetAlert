package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PhoneAlertRepositoryImpl implements PhoneAlertRepository {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;


    @Override
    public List<String> getPhonesByStationNumber(Integer stationNumber) {
        List<String> firestationsAddresses = firestationRepository.getByStation(stationNumber)
                .stream()
                .map(Firestation::getAddress)
                .toList();
        List<Person> personsByAddress = personRepository.getByAddresses(firestationsAddresses);
        Set<String> uniquePhonesByStation = personsByAddress
                .stream()
                .map(Person::getPhone)
                .filter(phone -> phone != null && !phone.isEmpty())
                .collect(Collectors.toSet());

        return new ArrayList<>(uniquePhonesByStation);
    }

}
