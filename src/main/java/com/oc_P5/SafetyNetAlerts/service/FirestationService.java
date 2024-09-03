package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.List;

public interface FirestationService {

    List<Firestation> getFirestationsService();

    Firestation getFirestationByAddressService(String address);

    void updateFirestationMappingService(Firestation firestation);

    void addFirestationMappingService(Firestation firestation);

    void deleteFirestationMappingByAddressService(String address);

    void deleteFirestationMappingByStationService(Integer stationNumber);

    PersonsByStation getPersonsByStationService(Integer stationNumber);

}
