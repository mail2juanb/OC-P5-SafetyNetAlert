package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.service.ChildAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChildAlertController {

    private final ChildAlertService childAlertService;


    /**
     * GET http://localhost:8080/childAlert?address=<address>
     * Cet url doit retourner la liste des enfants habitant à l'adresse indiquée.
     * La liste de comprendre : firstName (String), lastName (String), age (Integer), personsInFamily(List<Person>)
     * @param address (String) adresse concernée
     * @return Liste de ChildrenByAddress OR empty
     */

    @GetMapping("/childAlert")
    public List<ChildrenByAddress> getChildByAddress(@RequestParam("address") String address) {
        return childAlertService.getChildByAddress(address);
    }

}
