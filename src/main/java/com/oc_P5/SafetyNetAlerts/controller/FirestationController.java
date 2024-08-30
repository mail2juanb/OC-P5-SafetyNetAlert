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

        log.info("CONTROLLER - getFirestationsController");

        return firestationService.getFirestationsService();
    }

    /**
     * PUT http://localhost:8080/firestation
     * //@param  stationNumber Numéro de la caserne de pompiers - String
     * //@param firestationAdress Adresse de la caserne de pompiers - String
     * @param firestation un object Firestation contenant : stationNumber, firestationAdress
     * @return Mettre à jour le numéro de la caserne de pompiers d'une adresse - ResponseEntity
     */
    @PutMapping("/firestation")
    public ResponseEntity<String> updateFirestationMappingController(@RequestBody Firestation firestation) {

        log.info("CONTROLLER - updateFirestationMappingController - firestationAdress : " + firestation.getAddress() + " - stationNumber : " + firestation.getStation());

        String messUpdate = "Numéro de caserne mis à jour pour l'adresse : " + firestation.getAddress() + " -- Numéro : " + firestation.getStation();

        if (firestationService.updateFirestationMappingService(firestation)) {
            return new ResponseEntity<>("SUCCESS --- " + messUpdate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL --- " + messUpdate, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST http://localhost:8080/firestation
     * //@param  stationNumber Numéro de la caserne de pompiers - String
     * //@param firestationAdress Adresse de la caserne de pompiers - String
     * @param firestation un object Firestation contenant : stationNumber, firestationAdress
     * @return Ajout d'un mapping caserne/adresse - ResponseEntity
     */
    @PostMapping("/firestation")
    public ResponseEntity<String> addFirestationMappingController(@RequestBody Firestation firestation) {

        log.info("CONTROLLER - addFirestationMappingController - firestationAdress : " + firestation.getAddress() + " - stationNumber : " + firestation.getStation());

        String messAdd = "Mapping caserne/adress ajouté pour l'adresse : " + firestation.getAddress() + " -- Numéro : " + firestation.getStation();

        if (firestationService.getFirestationByAddressService(firestation.getAddress())) {
            return new ResponseEntity<>("FAIL --- " + messAdd + ". \nL'adresse de la caserne existe déjà", HttpStatus.BAD_REQUEST);
        } else {
            if (firestationService.addFirestationMappingService(firestation)) {
                return new ResponseEntity<>("SUCCESS --- " + messAdd, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("FAIL --- " + messAdd, HttpStatus.BAD_REQUEST);
            }
        }
    }


    /**
     * DELETE http://localhost:8080/firestation?address=<adresse>
     * DELETE http://localhost:8080/firestation?stationNumber=<station_number>
     * @param address (optionnel) Adresse de la caserne de pompiers à supprimer
     * @param stationNumber (optionnel) Numéro de la station de pompiers à supprimer
     * @return Confirmation de la suppression du mapping - ResponseEntity
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<String> deleteFirestationMappingController (
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "stationNumber", required = false) Integer stationNumber) {

        log.info("CONTROLLER - deleteFirestationMappingController - firestationAdress : " + address + " - stationNumber = " + stationNumber);

        String messDelAddress = "Suppression de la caserne ayant pour adresse : " + address;
        String messDelStation = "Suppression de ou des casernes ayant pour numéro de Station : " + stationNumber;
        String messDel = "Cas non géré -- adresse = " + address + " // stationNumber = " + stationNumber;

        if (address == null && stationNumber != null) {
            if (firestationService.deleteFirestationMappingByStationService(stationNumber)) {
                return new ResponseEntity<>("SUCCESS --- " + messDelStation, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("FAIL --- " + messDelStation, HttpStatus.NOT_FOUND);
            }
        } else if (address != null && stationNumber == null) {
            if (firestationService.deleteFirestationMappingByAddressService(address)) {
                return new ResponseEntity<>("SUCCESS --- " + messDelAddress, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("FAIL --- " + messDelAddress, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("FAIL --- " + messDel, HttpStatus.NOT_FOUND);
        }

    }


    /**
     * http://localhost:8080/firestation?stationNumber=<station_number>
     * Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers
     * correspondante. Donc, si le numéro de station = 1, elle doit renvoyer les habitants couverts par la station numéro 1.
     * La liste doit inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone.
     * De plus, elle doit fournir un décompte du nombre d'adultes et du
     * nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone desservie.
     * @param stationNumber Numéro de la caserne de pompiers
     * @return Liste de personnes et le décompte des adultes et des enfants.
    */
    @GetMapping("/firestation")
    public PersonsByStation getPersonsByStationController(@RequestParam("stationNumber") Integer stationNumber) {

        log.info("CONTROLLER - getPersonsByStation" + " - stationNumber = " + stationNumber);

        return firestationService.getPersonsByStationService(stationNumber);

    }
}
