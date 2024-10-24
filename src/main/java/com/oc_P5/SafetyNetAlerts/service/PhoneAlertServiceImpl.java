package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
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

    @Override
    public List<String> getPhonesByStation(Integer stationNumber) {
        if(!firestationRepository.existsByStation(stationNumber)) {
            throw new NotFoundException("station " + stationNumber + " does not exists");
        }

        List<String> firestationsAddresses = firestationRepository.getByStation(stationNumber)
                .stream()
                .map(Firestation::getAddress)
                .toList();

        List<String> addressesNotFound = firestationsAddresses
                .stream()
                .filter(address -> personRepository.getByAddress(address).isEmpty())
                .toList();

        if(!addressesNotFound.isEmpty()) {
            throw new NotFoundException("Addresses covered by station : " + stationNumber + ", are not found with addresses : " + addressesNotFound);
        }

        List<Person> personsByAddress = personRepository.getByAddresses(firestationsAddresses);

        return personsByAddress
                .stream()
                .map(Person::getPhone)
                .distinct()
                .toList();
    }

}