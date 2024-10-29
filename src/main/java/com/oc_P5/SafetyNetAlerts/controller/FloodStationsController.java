package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.MemberByStation;
import com.oc_P5.SafetyNetAlerts.service.FloodStationsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class FloodStationsController {

    private final FloodStationsServiceImpl floodStationsService;


    /**
     * GET /flood/stations?stations
     * This url should return a list of all households served by the fire station.
     * This list must group people by address. It should also include the name, phone number and age,
     * with medical history (medications, dosage and allergies) next to each name.
     *
     * @param stationNumbers List of Integer station_Numbers validated with @NotNull @Size(min=1) and Integers @NotNull @Positive
     * @return ResponseEntity<List<MemberByStation>> (HttpStatus.OK)
     *
     */

    @Operation(summary = "Retrieve households served by firestation(s)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Households successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberByStation[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @GetMapping("/flood/stations")
    public ResponseEntity<List<MemberByStation>> getMembersByStation(
            @Valid @RequestParam("stationNumbers")
            @NotNull(message = "stationNumbers cannot be null")
            @Size(min = 1, message = "stationNumbers cannot be empty")
            List<@NotNull(message = "stationNumber cannot be null")
                @Positive(message = "stationNumber must be positive")
                Integer> stationNumbers) {

        List<MemberByStation> memberByStationList = floodStationsService.getMembersByStation(stationNumbers);

        return new ResponseEntity<>(memberByStationList, HttpStatus.OK);
    }

}