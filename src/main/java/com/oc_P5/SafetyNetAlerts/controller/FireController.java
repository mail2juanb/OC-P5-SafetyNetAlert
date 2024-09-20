package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsByAddress;
import com.oc_P5.SafetyNetAlerts.service.FireServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FireController {

    private final FireServiceImpl fireService;

    public FireController(FireServiceImpl fireService) {
        this.fireService = fireService;
    }

    /**
     * GET http://localhost:8080/fire?address=<address>
     * @param address Adresse filtre demandée
     * @return Liste de Person vivant à l'address demandée ainsi que le numéro de caserne couvrant.
     * mise en forme : String lastname, String phone, Integer age, List<String>medications, List<String>allergies
     */
    @GetMapping("/fire")
    public FirePersonsByAddress getFirePersonsByAddressController(@RequestParam("address") String address) {
        return fireService.getFirePersonsByAddress(address);
    }

}
