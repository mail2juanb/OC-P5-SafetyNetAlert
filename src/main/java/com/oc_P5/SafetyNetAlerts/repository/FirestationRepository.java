package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;

import java.util.Optional;
import java.util.List;

public interface FirestationRepository {
    List<Firestation> getFirestations();

    List<Firestation> getFirestationsByStation(Integer station);
    
}
