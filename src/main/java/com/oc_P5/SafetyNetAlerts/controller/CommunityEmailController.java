package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.service.CommunityEmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CommunityEmailController {

    private final CommunityEmailServiceImpl communityEmailService;

    public CommunityEmailController(CommunityEmailServiceImpl communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    /**
     * GET http://localhost:8080/communityEmail?city=<city>
     * Cet url doit retourner la liste d'email de chaque personnes d'une ville demandée
     * @param city nom de la ville
     * @return Liste de email
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmailByCityController(@RequestParam("city") String city) {
        return communityEmailService.getCommunityEmailByCity(city);
    }

}
