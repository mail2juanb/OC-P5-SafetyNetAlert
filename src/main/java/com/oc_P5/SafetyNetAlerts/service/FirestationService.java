package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.List;

public interface FirestationService {

    List<Firestation> getFirestationsService();

    void updateFirestationMappingService(Firestation firestation);

    void addFirestationMappingService(Firestation firestation);

    void deleteFirestationMappingService(String address, Integer stationNumber);

    PersonsByStation getPersonsByStationService(Integer stationNumber);

}
