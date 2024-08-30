package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.List;

public interface FirestationService {

    List<Firestation> getFirestationsService();

    boolean getFirestationByAddressService(String address);

    boolean updateFirestationMappingService(Firestation firestation);

    boolean addFirestationMappingService(Firestation firestation);

    boolean deleteFirestationMappingByAddressService(String address);

    boolean deleteFirestationMappingByStationService(Integer stationNumber);

    PersonsByStation getPersonsByStationService(Integer stationNumber);

}
