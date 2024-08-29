package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.List;

public interface FirestationRepository {

    List<Firestation> getFirestations();

    boolean updateFirestationMapping(Firestation firestation);

    boolean addFirestationMapping(Firestation firestation);

    boolean deleteFirestationMappingByAddress(String address);

    boolean deleteFirestationMappingByStation(Integer stationNumber);

    List<Firestation> getFirestationsByStation(Integer stationNumber);

}
