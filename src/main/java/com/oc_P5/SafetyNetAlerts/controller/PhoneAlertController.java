package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.service.PhoneAlertServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
     * This url should return a list of telephone numbers of residents served by the firestation.
     * We'll use it to send emergency text messages to specific households.
     * If no stationNumber is found in our file, the application will simply return an empty JSON object.
     *
     * @param stationNumber Integer firestation_number validated with @NotNull @Positive
     * @return ResponseEntity<List<String>> (HttpStatus.OK)
     */

    @Operation(summary = "Phone number of residents served by firestation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phones number successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonInfoLastName[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhonesByStationNumber(
            @Valid @RequestParam("stationNumber")
            @NotNull(message = "stationNumber cannot be null")
            @Positive(message = "stationNumber must be positive")
            Integer stationNumber) {

        List<String> phonesByStationNumber =  phoneAlertService.getPhonesByStation(stationNumber);

        return new ResponseEntity<>(phonesByStationNumber, HttpStatus.OK);
    }

}
