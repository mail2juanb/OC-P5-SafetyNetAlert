package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FirestationRepositoryImpl implements FirestationRepository {

    private final DataReader dataReaderService;


    @Override
    public List<Firestation> getFirestations() {
        return dataReaderService.getData().getFireStations();
    }

    @Override
    public boolean updateFirestationMapping(Firestation targetFirestation) {

        List<Firestation> firestations = getFirestations();

        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(targetFirestation.getAddress())) {
                Integer oldfirestationStation = firestation.getStation();
                firestation.setStation(targetFirestation.getStation());
                log.info("Mise à jour du numéro de la caserne de pompiers réussie pour l'adresse : {} -- " +
                        "Ancien numéro : {}  //  Nouveau numéro : {}", targetFirestation.getAddress(), oldfirestationStation, targetFirestation.getStation());
                return true;
            }
        }

        log.warn("Aucune caserne de pompiers trouvée pour l'adresse : {}", targetFirestation.getAddress());
        return false;
    }

    @Override
    public List<Firestation> getFirestationsByStation(Integer station) {
        return getFirestations()
                .stream()
                .filter(Firestation -> Firestation.getStation().equals(station))
                .toList();
    }
}
