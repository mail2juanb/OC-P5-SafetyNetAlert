package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.List;
import java.util.Optional;

public interface FirestationRepository {

    List<Firestation> getFirestations();

    Optional<Firestation> findFirestationByAddressByStation(Firestation firestation);

    boolean firestationByAddressByStationExists(Firestation firestation);

    Optional<Firestation> findFirestationByAddress(String address);

    boolean firestationByAddressExists(String address);

    Optional<Firestation> findFirestationByStation(Integer stationNumber);

    boolean firestationByStationExists(Integer stationNumber);

    Optional<Firestation> updateFirestationMapping(Firestation firestation);

    void addFirestationMapping(Firestation firestation);

    void deleteFirestationMapping(Firestation firestation);

    void deleteFirestationMappingByAddress(String address);

    void deleteFirestationMappingByStation(Integer stationNumber);

    List<Firestation> getFirestationsByStation(Integer stationNumber);

}
