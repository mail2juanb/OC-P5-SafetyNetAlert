package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.service.FirestationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
//@RequestMapping("/firestation")
public class FirestationController {

    private final FirestationServiceImpl firestationService;


    public FirestationController(FirestationServiceImpl firestationService) {
        this.firestationService = firestationService;
    }


    /*
    http://localhost:8080/firestation?stationNumber=<station_number>
    Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers
    correspondante. Donc, si le numéro de station = 1, elle doit renvoyer les habitants
    couverts par la station numéro 1.
    La liste doit inclure les informations spécifiques suivantes :
        - prénom
        - nom
        - adresse
        - numéro de téléphone
    De plus, elle doit fournir un décompte du nombre d'adultes et du nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone desservie.
*/
    @GetMapping("/firestation")
    public PersonsByStation getPersonsByStationController(@RequestParam("stationNumber") Integer stationNumber) {
        //public void getPersonsByStationController(@RequestParam("stationNumber") Integer stationNumber) {

        log.info("CONTROLLER - getPersonsByStation" + " - stationNumber = " + stationNumber);

        return firestationService.getPersonsByStationService(stationNumber);

        /*
        // Map de réponse à la requête
        List<Object> response = new ArrayList<>();

        // Appel aux méthodes spécifiques à cette requête vers la couche service
        //    On récupère la Map des personnes couvertes par la caserne demandée
        FirestationServiceImpl firestationService = new FirestationServiceImpl();
        Map<String, Object> mapPersonsByStationService = firestationService.getPersonsByStationService(stationNumber);


        // On récupère la Map des Adultes
        //Map<String, Object> mapPersonsAdultService =
        PersonAgeService personAgeService = new PersonAgeService();
        personAgeService.countChildAndAdult(mapPersonsByStationService);



        response.add(mapPersonsByStationService);
        //response.add(mapPersonsAdultService);
        //response.add(mapPersonsChildService);


        return response;

         */
    }
}
