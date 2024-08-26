package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;

public interface FirestationService {

    PersonsByStation getPersonsByStationService(Integer stationNumber);
}
