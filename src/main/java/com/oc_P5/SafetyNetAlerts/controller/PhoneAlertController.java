package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.service.PhoneAlertServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PhoneAlertController {

    private final PhoneAlertServiceImpl phoneAlertService;


    public PhoneAlertController(PhoneAlertServiceImpl phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    /**
     * GET http://localhost:8080/phoneAlert?firestation=<firestation_number>
     * Cet url doit retourner la liste de numéros de téléphone des personnes desservis par la caserne de pompier
     * @param firestation_Number numéro de la Station
     * @return Liste de numéros de téléphone
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhonesByStationNumberController(@RequestParam("firestation_Number") Integer firestation_Number) {
        return phoneAlertService.getPhonesByStation(firestation_Number);
    }

}
