package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.service.CommunityEmailServiceImpl;
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
public class CommunityEmailController {

    private final CommunityEmailServiceImpl communityEmailService;

    /**
     * GET http://localhost:8080/communityEmail?city=<city>
     * Cet url doit retourner la liste d'email de chaque personne d'une ville demandée
     * @param city nom de la ville
     * @return ResponseEntity<List<String>> liste d'email de chaque personne
     * @throws NotFoundException si la ville est introuvable
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getCommunityEmailByCity(
            @Valid @RequestParam("city")
            @NotBlank(message = "city can't be blank")
            String city) {

        List<String> communityEmail = communityEmailService.getCommunityEmailByCity(city);

        return new ResponseEntity<>(communityEmail, HttpStatus.OK);
    }

}
