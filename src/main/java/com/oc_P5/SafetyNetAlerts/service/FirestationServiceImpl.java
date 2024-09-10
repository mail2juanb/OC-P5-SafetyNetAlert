package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
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


    public List<Firestation> getFirestationsService() {
        return firestationRepository.getFirestations();
    }

    public void updateFirestationMappingService(Firestation firestation) {
        if(!firestationRepository.firestationByAddressExists(firestation.getAddress())){
            throw new NotFoundException("Firestation doesn't exist");
        }
        firestationRepository.updateFirestationMapping(firestation);
    }

    public void addFirestationMappingService(Firestation firestation) {
        if(firestationRepository.firestationByAddressExists(firestation.getAddress())){
            throw new ConflictException("Firestation already exists");
        }
        firestationRepository.addFirestationMapping(firestation);
    }


    public void deleteFirestationMappingService(String address, Integer stationNumber) {
        if (address == null && stationNumber == null) {
            throw new ConflictException("Both address and station number can't be null");
        }

        if (address != null && stationNumber != null) {
            Firestation deleteFirestation = new Firestation(address, stationNumber);
            if (firestationRepository.firestationByAddressByStationExists(deleteFirestation)) {
                firestationRepository.deleteFirestationMapping(deleteFirestation);
                return;
            } else {
                throw new NotFoundException("Firestation doesn't exist");
            }
        }

        if (address != null) {
            if (firestationRepository.firestationByAddressExists(address)) {
                firestationRepository.deleteFirestationMappingByAddress(address);
                return;
            } else {
                throw new NotFoundException("Address doesn't exist");
            }
        }

        // FIXME Comment faire pour mieux tester cette méthode et passer à 100% de couverture ?
        if (stationNumber != null) {
            if (firestationRepository.firestationByStationExists(stationNumber)) {
                firestationRepository.deleteFirestationMappingByStation(stationNumber);
            } else {
                throw new NotFoundException("Station number doesn't exist");
            }
        }
    }

    public PersonsByStation getPersonsByStationService(Integer stationNumber) {
        Set<String> stationAddress = firestationRepository.getFirestationsByStation(stationNumber)
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