package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.FirestationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class FirestationController {

    private final FirestationServiceImpl firestationService;


    public FirestationController(FirestationServiceImpl firestationService) {
        this.firestationService = firestationService;
    }


    /**
     * http://localhost:8080/firestations
     * @return Liste des Firestation ainsi que leurs attributs
     */
    @GetMapping("/firestations")
    public List<Firestation> getFirestationsController() {

        log.info("CONTROLLER - getFirestationsController");

        return firestationService.getFirestationsService();
    }


    /**
     * http://localhost:8080/firestation?stationNumber=<station_number>
     * Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers
     * correspondante. Donc, si le numéro de station = 1, elle doit renvoyer les habitants
     * couverts par la station numéro 1.
     * La liste doit inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone.
     * De plus, elle doit fournir un décompte du nombre d'adultes et du
     * nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone desservie.
     * @param stationNumber Firestation Number
     * @return Liste de personnes et le décompte des adultes et des enfants.
    */
    @GetMapping("/firestation")
    public PersonsByStation getPersonsByStationController(@RequestParam("stationNumber") Integer stationNumber) {

        log.info("CONTROLLER - getPersonsByStation" + " - stationNumber = " + stationNumber);

        return firestationService.getPersonsByStationService(stationNumber);

    }
}
