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
     * GET /phoneAlert?firestation
     * This url should return a list of telephone numbers of residents served by the fire station.
     * We'll use it to send emergency text messages to specific households.
     *
     * @param firestation_number Integer firestation_number validated with @NotNull @Positive
     * @return ResponseEntity<List<String>> (HttpStatus.OK)
     * @throws NotFoundException if the station number cannot be found Or if firestation address doesn't exist in PersonRepository
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
