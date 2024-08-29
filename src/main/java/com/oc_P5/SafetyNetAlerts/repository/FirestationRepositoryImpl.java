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
    public boolean addFirestationMapping(Firestation newFirestation) {

        List<Firestation> firestations = getFirestations();
        firestations.add(newFirestation);
        log.info("Nouvelle caserne ajoutée : Adresse = {}, Station = {}", newFirestation.getAddress(), newFirestation.getStation());
        return true;
    }

    @Override
    public boolean deleteFirestationMappingByAddress(String address) {

        List<Firestation> firestations = getFirestations();

        if (firestations.removeIf(firestation -> firestation.getAddress().equals(address))) {
            log.info("La caserne : {}, a été supprimée", address);
            return true;
        } else {
            log.info("La caserne : {}, n'a pas été supprimée", address);
            return false;
        }

    }

    @Override
    public boolean deleteFirestationMappingByStation(Integer stationNumber) {

        List<Firestation> firestations = getFirestations();
        int firestationRemovedNumber = 0;
        boolean isRemoved = false;

        for (Firestation firestation : firestations) {
            if (firestation.getStation().equals(stationNumber)) {
                firestationRemovedNumber ++;
            }
        }
        if (firestationRemovedNumber > 0) {
            log.info("Les casernes ayant pour stationNumber : {}, au nombre de : {}, ont été supprimées", stationNumber, firestationRemovedNumber);
            isRemoved = firestations.removeIf(firestation -> firestation.getStation().equals(stationNumber));
        } else {
            log.info("Aucune caserne ayant pour stationNumber : {}, n'a été supprimée", stationNumber);
        }

        return isRemoved;

    }


    @Override
    public List<Firestation> getFirestationsByStation(Integer stationNumber) {
        return getFirestations()
                .stream()
                .filter(Firestation -> Firestation.getStation().equals(stationNumber))
                .toList();
    }
}
