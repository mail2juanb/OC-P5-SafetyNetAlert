package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.service.PhoneAlertServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PhoneAlertController {

    private final PhoneAlertServiceImpl phoneAlertService;


    /**
     * GET http://localhost:8080/phoneAlert?firestation=<firestation_number>
     * Cette url doit retourner une liste des numéros de téléphone des résidents desservis
     * par la caserne de pompiers. Nous l'utiliserons pour envoyer des messages texte
     * d'urgence à des foyers spécifiques.
     * @param firestation_number numéro de la Station
     * @return Liste de numéros de téléphone
     * @throws NotFoundException si le numéro de station est introuvable
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhonesByStationNumber(
            @Valid @RequestParam("firestation_number")
            @NotNull(message = "firestation_number cannot be null")
            @Positive(message = "firestation_number must be positive")
            Integer firestation_number) {

        List<String> phonesByStationNumber =  phoneAlertService.getPhonesByStation(firestation_number);

        return new ResponseEntity<>(phonesByStationNumber, HttpStatus.OK);
    }

}
