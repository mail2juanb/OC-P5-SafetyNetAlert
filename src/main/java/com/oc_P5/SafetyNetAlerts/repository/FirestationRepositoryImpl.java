package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FirestationRepositoryImpl implements FirestationRepository {

    private final DataReader dataReaderService;


    @Override
    public List<Firestation> getFirestations() {
        return dataReaderService.getData().getFireStations();
    }

    @Override
    public Optional<Firestation> findFirestationByAddressByStation(Firestation firestation) {
        return getFirestations()
                .stream()
                .filter(f -> f.getAddress().equals(firestation.getAddress()) &&
                        f.getStation().equals(firestation.getStation()))
                .findFirst();
    }

    public boolean firestationByAddressByStationExists(Firestation firestation) {
        return findFirestationByAddressByStation(firestation)
                .isPresent();
    }

    @Override
    public Optional<Firestation> findFirestationByAddress(String address) {
        return getFirestations()
                .stream()
                .filter(f -> f.getAddress().equals(address))
                .findFirst();
    }

    @Override
    public boolean firestationByAddressExists(String address){
        return findFirestationByAddress(address)
                .isPresent();
    }

    @Override
    public Optional<Firestation> findFirestationByStation(Integer stationNumber) {
        return getFirestations()
                .stream()
                .filter(f -> f.getStation().equals(stationNumber))
                .findFirst();
    }

    @Override
    public boolean firestationByStationExists(Integer stationNumber) {
        return findFirestationByStation(stationNumber)
                .isPresent();
    }

    @Override
    public Optional<Firestation> updateFirestationMapping(Firestation targetFirestation) {
        return findFirestationByAddress(targetFirestation.getAddress())
                .map(firestation -> {
                    firestation.setStation(targetFirestation.getStation());
                    return firestation;
                });
    }

    @Override
    public void addFirestationMapping(Firestation newFirestation) {
        List<Firestation> firestations = getFirestations();
        firestations.add(newFirestation);
    }

    @Override
    public void deleteFirestationMapping(Firestation deleteFirestation) {
        List<Firestation> firestations = getFirestations();
        firestations.removeIf(firestation -> firestation.getAddress().equals(deleteFirestation.getAddress()) &&
                firestation.getStation().equals(deleteFirestation.getStation()));
    }

    @Override
    public void deleteFirestationMappingByAddress(String address) {
        List<Firestation> firestations = getFirestations();
        firestations.removeIf(firestation -> firestation.getAddress().equals(address));
    }

    @Override
    public void deleteFirestationMappingByStation(Integer stationNumber) {
        List<Firestation> firestations = getFirestations();
        firestations.removeIf(firestation -> firestation.getStation().equals(stationNumber));
    }

    @Override
    public List<Firestation> getFirestationsByStation(Integer stationNumber) {
        return getFirestations()
                .stream()
                .filter(Firestation -> Firestation.getStation().equals(stationNumber))
                .toList();
    }

}
