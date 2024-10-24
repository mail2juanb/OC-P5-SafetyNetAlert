package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.NamedModel;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirestationServiceImpl implements FirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;


    @Override
    public PersonsByStation getPersonsByStation(Integer station_number) {

        if(!firestationRepository.existsByStation(station_number)) {
            throw new NotFoundException("station_number doesn't exist with station_number = " + station_number);
        }

        Set<String> stationAddresses = firestationRepository.getByStation(station_number)
                .stream()
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        List<Person> personsByAddress = personRepository.getByAddresses(stationAddresses);

        Integer nbrOfMinor = personsByAddress
                .stream()
                .map(NamedModel::getId)
                .map(medicalRecordRepository::findById)
                .filter(Optional::isPresent)
                .filter(optionalMedicalrecord -> optionalMedicalrecord.get().isMinor())
                .toList()
                .size();

        return new  PersonsByStation(personsByAddress, nbrOfMinor );
    }



    @Override
    public void updateFirestation(Firestation firestation) {
        if(!firestationRepository.existsByAddress(firestation.getAddress())) {
            throw new NotFoundException("Firestation doesn't exist with address = " + firestation.getAddress());
        }
        firestationRepository.update(firestation);
    }


    @Override
    public void addFirestation(Firestation firestation) {
        if(firestationRepository.existsByAddress(firestation.getAddress())){
            throw new ConflictException("Firestation already exists with address = " + firestation.getAddress());
        }
        firestationRepository.save(firestation);
    }


    @Override
    public void deleteFirestationByAddress(String address) {
        if(!firestationRepository.existsByAddress(address)){
            throw new NotFoundException("Firestation doesn't exist with address = " + address);
        }

        firestationRepository.deleteByAddress(address);
    }

    @Override
    public void deleteFirestationByStation(Integer station_number) {
        if(!firestationRepository.existsByStation(station_number)){
            throw new NotFoundException("Firestation doesn't exist with station = " + station_number.toString());
        }

        firestationRepository.deleteByStation(station_number);
    }


}