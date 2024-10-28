package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.List;
import java.util.Optional;

public interface FirestationRepository {

    List<Firestation> getAll();

    Optional<Firestation> findByAddressByStation(Firestation firestation);

    boolean existsByAddressByStation(Firestation firestation);

    Optional<Firestation> findByAddress(String address);

    boolean existsByAddress(String address);

    Optional<Firestation> findByStation(Integer station_number);

    boolean existsByStation(Integer station_number);

    void update(Firestation firestation);

    void save(Firestation firestation);

    void deleteByAddress(String address);

    void deleteByStation(Integer stationNumber);

    List<Firestation> getByStation(Integer stationNumber);

}
