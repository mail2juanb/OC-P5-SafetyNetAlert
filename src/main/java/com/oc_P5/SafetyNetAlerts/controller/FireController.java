package com.oc_P5.SafetyNetAlerts.controller;


import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.service.FireServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FireController {

    private final FireServiceImpl fireService;



    /**
     * GET http://localhost:8080/fire?address=<address>
     * @param address Adresse filtre demandée
     * @return ResponseEntity<FirePersonsResponse> Liste de Person vivant à l'address demandée ainsi que le numéro de caserne couvrant.
     * Response mise en forme : String lastname, String phone, Integer age, List<String>medications, List<String>allergies
     * @throws NotFoundException Si l'address ne correspond à aucune firestation
     * @throws NotFoundException Si l'address ne correspond à aucune person
     */
    @GetMapping("/fire")
    public ResponseEntity<FirePersonsResponse> getFirePersonsByAddress(
            @Valid @RequestParam("address")
            @NotBlank(message = "address cannot be blank")
            String address) {

        FirePersonsResponse firePersonsByAddress = fireService.getFirePersonsByAddress(address);

        return new ResponseEntity<>(firePersonsByAddress, HttpStatus.OK);
    }

}
