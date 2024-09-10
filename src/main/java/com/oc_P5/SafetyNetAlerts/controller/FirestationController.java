package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.FirestationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class FirestationController {

    private final FirestationServiceImpl firestationService;



    public FirestationController(FirestationServiceImpl firestationService) {
        this.firestationService = firestationService;
    }



    /**
     * GET http://localhost:8080/firestations
     * @return Liste des Firestation ainsi que leurs attributs
     */
    @GetMapping("/firestations")
    public List<Firestation> getFirestationsController() {
        return firestationService.getFirestationsService();
    }



    /**
     * GET http://localhost:8080/firestation?stationNumber=<station_number>
     * Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers
     * correspondante. Donc, si le numéro de station = 1, elle doit renvoyer les habitants couverts par la station numéro 1.
     * La liste doit inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone.
     * De plus, elle doit fournir un décompte du nombre d'adultes et du
     * nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone desservie.
     * @param stationNumber Numéro de la caserne de pompiers
     * @return Liste de PersonsByStation et le décompte des adultes et des enfants.
     */
    @GetMapping("/firestation")
    public PersonsByStation getPersonsByStationController(@RequestParam("stationNumber") Integer stationNumber) {
        return firestationService.getPersonsByStationService(stationNumber);
    }



    /**
     * PUT http://localhost:8080/firestation
     * @param firestation un object Firestation contenant : stationNumber, firestationAdress
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PutMapping("/firestation")
    public ResponseEntity<String> updateFirestationMappingController(@RequestBody Firestation firestation) {
        firestationService.updateFirestationMappingService(firestation);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * POST http://localhost:8080/firestation
     * @param firestation un object Firestation contenant : stationNumber, firestationAdress
     * @return ResponseEntity<>(HttpStatus.CREATED)
     */
    @PostMapping("/firestation")
    public ResponseEntity<String> addFirestationMappingController(@RequestBody Firestation firestation) {
        firestationService.addFirestationMappingService(firestation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * DELETE http://localhost:8080/firestation?address=<adresse>
     * DELETE http://localhost:8080/firestation?stationNumber=<station_number>
     * @param address (optionnel) Adresse de la caserne de pompiers à supprimer
     * @param stationNumber (optionnel) Numéro de la station de pompiers à supprimer
     * @return null
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<String> deleteFirestationMappingController (
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "stationNumber", required = false) Integer stationNumber) {
        firestationService.deleteFirestationMappingService(address, stationNumber);
        // FIXME moi je dirais plutot ca, non ? return new ResponseEntity<>(HttpStatus.OK);
        return null;

    }




}
