package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;


public interface FirestationService {

    PersonsByStation getPersonsByStation(Integer station_number);

    void updateFirestation(Firestation firestation);

    void addFirestation(Firestation firestation);

    void deleteFirestationByAddress(String address);

    void deleteFirestationByStation(Integer station_number);

}
