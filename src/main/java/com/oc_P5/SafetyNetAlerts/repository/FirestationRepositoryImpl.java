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
    public List<Firestation> getAll() {
        return dataReaderService.getData().getFireStations();
    }

    @Override
    public Optional<Firestation> findByAddressByStation(Firestation firestation) {
        return getAll()
                .stream()
                .filter(f -> f.getAddress().equals(firestation.getAddress()) &&
                        f.getStation().equals(firestation.getStation()))
                .findFirst();
    }

    public boolean existsByAddressByStation(Firestation firestation) {
        return findByAddressByStation(firestation)
                .isPresent();
    }

    @Override
    public Optional<Firestation> findByAddress(String address) {
        return getAll()
                .stream()
                .filter(f -> f.getAddress().equals(address))
                .findFirst();
    }

    @Override
    public boolean existsByAddress(String address){
        return findByAddress(address)
                .isPresent();
    }

    @Override
    public Optional<Firestation> findByStation(Integer station_number) {
        return getAll()
                .stream()
                .filter(f -> f.getStation().equals(station_number))
                .findFirst();
    }

    @Override
    public boolean existsByStation(Integer station_number) {
        return findByStation(station_number)
                .isPresent();
    }

    @Override
    public void update(Firestation updateFirestation) {
        Firestation firestationToUpdate = findByAddress(updateFirestation.getAddress()).orElseThrow();
        int index = getAll().indexOf(firestationToUpdate);
        getAll().set(index, updateFirestation);
    }

    @Override
    public void save(Firestation addFirestation) {
        List<Firestation> firestationList = getAll();
        firestationList.add(addFirestation);
    }

    @Override
    public void deleteByAddress(String address) {
        List<Firestation> firestations = getAll();
        firestations.removeIf(firestation -> firestation.getAddress().equals(address));
    }

    @Override
    public void deleteByStation(Integer station_number) {
        List<Firestation> firestations = getAll();
        firestations.removeIf(firestation -> firestation.getStation().equals(station_number));
    }

    @Override
    public List<Firestation> getByStation(Integer station_number) {
        return getAll()
                .stream()
                .filter(Firestation -> Firestation.getStation().equals(station_number))
                .toList();
    }

}
