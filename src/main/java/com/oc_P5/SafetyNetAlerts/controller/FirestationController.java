package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.controller.requests.FirestationRequest;
import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.FirestationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class FirestationController {

    private final FirestationServiceImpl firestationService;



    /**
     * GET /firestation?stationNumber
     * This url should return a list of people covered by the corresponding fire station.
     * So, if station number = 1, it should return the residents covered by station number 1.
     * The list must include the following specific information: first name, last name, address, telephone number.
     * In addition, it must provide a count of the number of adults and children number of children
     * (any individual aged 18 or under) in the area served.
     * If no address is found in our file, the application will simply return an empty JSON object.
     *
     * @param stationNumber Firestation number (Integer validated with @NotNull and @Positive)
     * @return ResponseEntity<PersonsByStation> (HttpStatus.OK)
     */

    @Operation(summary = "Retrieve persons covered by a Firestation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persons successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonsByStation.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @GetMapping("/firestation")
    public ResponseEntity<PersonsByStation> getPersonsByStation(
            @Valid @RequestParam("stationNumber")
            @NotNull(message = "stationNumber cannot be null")
            @Positive(message = "stationNumber must be positive")
            Integer stationNumber) {

        PersonsByStation personsByStation = firestationService.getPersonsByStation(stationNumber);

        return new ResponseEntity<>(personsByStation, HttpStatus.OK);
    }



    /**
     * PUT /firestation
     * Update an address's firestation number
     *
     * @param updateFirestationRequest FirestationRequest validated with :
     *                                 String address @NotBlank,
     *                                 Integer stationNumber @NotNull @Positive
     * @return ResponseEntity<?>(HttpStatus.OK)
     * @throws NotFoundException if firestation doesn't exist
     *
     */

    @Operation(summary = "Update a Firestation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firestation successfully updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request", content = @Content)
    })

    @PutMapping("/firestation")
    public ResponseEntity<?> updateFirestation(@Valid @RequestBody FirestationRequest updateFirestationRequest) {

        final Firestation firestation = new Firestation(updateFirestationRequest.getAddress(), updateFirestationRequest.getStationNumber());

        firestationService.updateFirestation(firestation);

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * POST /firestation
     * Add firestation
     *
     * @param addFirestationRequest FirestationRequest validated with :
     *                                 String address @NotBlank,
     *                                 Integer station_number @NotNull @Positive
     * @return ResponseEntity<?>(HttpStatus.CREATED)
     * @throws ConflictException if firestation already exists
     */

    @Operation(summary = "Add a Firestation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Firestation successfully added", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict: Firestation already exists", content = @Content)
    })

    @PostMapping("/firestation")
    public ResponseEntity<?> addFirestation(@Valid @RequestBody FirestationRequest addFirestationRequest) {

        final Firestation firestation = new Firestation(addFirestationRequest.getAddress(), addFirestationRequest.getStationNumber());

        firestationService.addFirestation(firestation);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * DELETE /firestation/address?address
     * Delete firestation associated with the specified address.
     *
     * @param address address validated with String address @NotBlank
     * @return ResponseEntity<?>(HttpStatus.OK)
     */

    @Operation(summary = "Delete Firestation by address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firestation successfully deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @DeleteMapping("/firestation/address")
    public ResponseEntity<?> deleteFirestationByAddress(
            @Valid @RequestParam ("address")
            @NotBlank(message = "address cannot be null or blank")
            String address) {

        firestationService.deleteFirestationByAddress(address);

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * DELETE /firestation/station?station_number
     * Delete firestation(s) associated with specified station_number.
     *
     * @param stationNumber station number validated with Integer stationNumber @NotNull @Positive
     * @return ResponseEntity<?>(HttpStatus.OK)
     */

    @Operation(summary = "Delete Firestations by station")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firestations successfully deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @DeleteMapping("/firestation/station")
    public ResponseEntity<?> deleteFirestationByStation(
            @Valid @RequestParam ("stationNumber")
            @NotNull(message = "stationNumber cannot be null")
            @Positive(message = "stationNumber must be positive")
            Integer stationNumber) {

        firestationService.deleteFirestationByStation(stationNumber);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}