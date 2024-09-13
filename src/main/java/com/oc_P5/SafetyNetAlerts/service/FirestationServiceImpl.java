package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirestationServiceImpl implements FirestationService{

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalrecordRepository;


    public boolean isFirestationEmpty(Firestation firestation) {
        return (firestation.getAddress() == null && firestation.getAddress().isEmpty()) &&
                (firestation.getStation() == null);
    }

    public List<Firestation> getFirestations() {
        return firestationRepository.getAll();
    }

    public void updateFirestation(Firestation firestation) {
        if(isFirestationEmpty(firestation)) {
            throw new NullOrEmptyObjectException("Firestation can not be null or empty");
        }
        Firestation updatedFirestation = firestationRepository.findByAddress(firestation.getAddress())
                        .orElseThrow(()-> new NotFoundException("Firestation doesn't exist with address = " + firestation.getAddress()))
                                .update(firestation);

        firestationRepository.update(updatedFirestation);
    }

    public void addFirestation(Firestation firestation) {
        if(isFirestationEmpty(firestation)) {
            throw new NullOrEmptyObjectException("Firestation can not be null or empty");
        }
        if(firestationRepository.existsByAddress(firestation.getAddress())){
            throw new ConflictException("Firestation already exists with address = " + firestation.getAddress());
        }
        firestationRepository.save(firestation);
    }


    public void deleteFirestation(Firestation firestation) {
        if(isFirestationEmpty(firestation)) {
            throw new NullOrEmptyObjectException("Firestation can not be null or empty");
        }
        if (firestation.getAddress() != null && firestation.getStation() != null) {
            if (firestationRepository.existsByAddressByStation(firestation)) {
                firestationRepository.delete(firestation);
                return;
            } else {
                throw new NotFoundException("Firestation doesn't exist with address = " + firestation.getAddress());
            }
        }
        if (firestation.getAddress() != null && firestation.getStation() == null) {
            if (firestationRepository.existsByAddress(firestation.getAddress())) {
                firestationRepository.deleteByAddress(firestation.getAddress());
                return;
            } else {
                throw new NotFoundException("Address doesn't exist with address = " + firestation.getAddress());
            }
        }
        if (firestation.getStation() != null && firestation.getAddress() == null) {
            if (firestationRepository.existsByStation(firestation.getStation())) {
                firestationRepository.deleteByStation(firestation.getStation());
            } else {
                throw new NotFoundException("StationNumber doesn't exist with stationNumber = " + firestation.getStation());
            }
        }
    }

    public PersonsByStation getPersonsByStationService(Integer stationNumber) {
        if(!firestationRepository.existsByStation(stationNumber)) {
            throw new NotFoundException("StationNumber doesn't exist with stationNumber = " + stationNumber);
        }
        Set<String> stationAddress = firestationRepository.getByStation(stationNumber)
                .stream()
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        List<Person> personsByAddress = personRepository.getPersonsByAddresses(stationAddress);
        Integer nbrOfMinor = personsByAddress
                .stream()
                .map(p -> p.getId())
                .map(id -> medicalrecordRepository.findById(id))
                .filter(optionalMedicalrecord -> optionalMedicalrecord.isPresent())
                .filter(optionalMedicalrecord -> optionalMedicalrecord.get().isMinor())
                .toList()
                .size();
        return new  PersonsByStation(personsByAddress, nbrOfMinor );
    }

}