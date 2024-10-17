package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.service.ChildAlertService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class ChildAlertController {

    private final ChildAlertService childAlertService;


    /**
     * GET http://localhost:8080/childAlert?address=<address>
     * Cet url doit retourner la liste des enfants habitant à l'adresse indiquée.
     * La liste doit comprendre : firstName (String), lastName (String), age (Integer), personsInFamily(List<Person>)
     * @param address (String) adresse concernée
     * @return ResponseEntity<List<ChildrenByAddress>> Liste de ChildrenByAddress OR empty
     */

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildrenByAddress>> getChildByAddress(
            @Valid @RequestParam("address")
            @NotBlank(message = "address cannot be blank")
            String address) {

         List<ChildrenByAddress> childrenByAddress = childAlertService.getChildByAddress(address);

         return new ResponseEntity<>(childrenByAddress, HttpStatus.OK);
    }

}
