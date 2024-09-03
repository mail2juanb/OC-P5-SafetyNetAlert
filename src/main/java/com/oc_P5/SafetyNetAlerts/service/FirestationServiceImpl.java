package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalrecordRepository;
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
    private final MedicalrecordRepository medicalrecordRepository;


        public List<Firestation> getFirestationsService() {
            return firestationRepository.getFirestations();
        }

        public Firestation getFirestationByAddressService(String address) {
            return firestationRepository.getFirestationByAddress(address);
        }

        public void updateFirestationMappingService(Firestation firestation) {
            firestationRepository.updateFirestationMapping(firestation);
        }

        public void addFirestationMappingService(Firestation firestation) {
            if(firestationRepository.firestationByAddressExists(firestation.getAddress())){
                throw new ConflictException("Firestation already exists");
            }

            firestationRepository.addFirestationMapping(firestation);
        }

        public void deleteFirestationMappingByAddressService(String address) {
            firestationRepository.deleteFirestationMappingByAddress(address);
        }

        public void deleteFirestationMappingByStationService(Integer stationNumber) {
            firestationRepository.deleteFirestationMappingByStation(stationNumber);
        }


        public PersonsByStation getPersonsByStationService(Integer stationNumber) {

            Set<String> stationAddress = firestationRepository.getFirestationsByStation(stationNumber)
                    .stream()
                    .map(Firestation::getAddress)
                    .collect(Collectors.toSet());

            List<Person> personsByAddress = personRepository.getPersonsByAddress(stationAddress);

            Integer nbrOfMinor = personsByAddress
                    .stream()
                    .map(p -> p.getId())
                    .map(id -> medicalrecordRepository.findMedicalrecordById(id))
                    .filter(optionalMedicalrecord -> optionalMedicalrecord.isPresent())
                    .filter(optionalMedicalrecord -> optionalMedicalrecord.get().isMinor())
                    .toList()
                    .size();

            return new  PersonsByStation(personsByAddress, nbrOfMinor );

        }

}