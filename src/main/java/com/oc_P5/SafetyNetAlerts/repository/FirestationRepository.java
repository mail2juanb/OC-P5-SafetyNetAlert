package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FirestationRepository {

    List<Firestation> getFirestations();

    Firestation getFirestationByAddress(String address);

    Optional<Firestation> findFirestationByAddress(String address);

    boolean firestationByAddressExists(String address);

    boolean updateFirestationMapping(Firestation firestation);

    boolean addFirestationMapping(Firestation firestation);

    boolean deleteFirestationMappingByAddress(String address);

    boolean deleteFirestationMappingByStation(Integer stationNumber);

    List<Firestation> getFirestationsByStation(Integer stationNumber);

}
