package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.List;

public interface FirestationService {

    List<Firestation> getFirestationsService();

    boolean updateFirestationMappingService(Firestation firestation);

    boolean addFirestationMappingService(Firestation firestation);

    PersonsByStation getPersonsByStationService(Integer stationNumber);

}
