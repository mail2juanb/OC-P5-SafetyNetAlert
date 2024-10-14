package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.controller.requests.AddFirestationRequest;
import com.oc_P5.SafetyNetAlerts.controller.requests.UpdateFirestationRequest;
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
     * GET http://localhost:8080/firestation?station_number=<station_number>
     * Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers
     * correspondante. Donc, si le numéro de station = 1, elle doit renvoyer les habitants couverts par la station numéro 1.
     * La liste doit inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone.
     * De plus, elle doit fournir un décompte du nombre d'adultes et du
     * nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone desservie.
     * @param station_number Numéro de la caserne de pompiers
     * @return ResponseEntity<PersonsByStation>(HttpStatus.OK)
     * Une réponse contenant une liste de PersonsByStation et le décompte des adultes et des enfants.
     * @throws NotFoundException si le numéro de station est invalide
     */

    @Operation(summary = "Retrieve persons covered by a Firestation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persons successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonsByStation.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request",
                    content = @Content),
    })

    @GetMapping("/firestation")
    public ResponseEntity<PersonsByStation> getPersonsByStation(
            @Valid
            @RequestParam("station_number")
            @NotNull(message = "station_number cannot be null")
            @Positive(message = "station_number must be positive")
            Integer station_number) {

        PersonsByStation personsByStation = firestationService.getPersonsByStation(station_number);

        return new ResponseEntity<>(personsByStation, HttpStatus.OK);
    }



    /**
     * PUT http://localhost:8080/firestation
     * Mettre à jour le numéro de la caserne de pompiers d'une adresse spécifiée
     * @param updateFirestationRequest un object UpdateFirestationRequest contenant : station_number, firestation
     * @return ResponseEntity<Void>(HttpStatus.OK)
     * @throws NotFoundException si la caserne de pompiers n'existe pas
     * @throws ConflictException si la caserne de pompiers existe déjà avec les paramètres donnés
     * // @throws ArgumentNotValidException
     */

    @Operation(summary = "Update a Firestation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firestation successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict: Firestation already exists with the given parameters",
                    content = @Content) })

    @PutMapping("/firestation")
    public ResponseEntity<Void> updateFirestation(@Valid @RequestBody UpdateFirestationRequest updateFirestationRequest) {

        final Firestation firestation = new Firestation(updateFirestationRequest.getAddress(), updateFirestationRequest.getStation_number());

        firestationService.updateFirestation(firestation);

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * POST http://localhost:8080/firestation
     * Ajout d'un mapping caserne/adresse
     * @param addFirestationRequest un object AddFirestationRequest contenant : station_number, firestation
     * @return ResponseEntity<String>(HttpStatus.CREATED)
     * @throws NotFoundException si la station de pompiers n'existe pas
     */

    @Operation(summary = "Add a Firestation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Firestation successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict: Firestation already exists",
                    content = @Content) })

    @PostMapping("/firestation")
    public ResponseEntity<String> addFirestation(@Valid @RequestBody AddFirestationRequest addFirestationRequest) {

        final Firestation firestation = new Firestation(addFirestationRequest.getAddress(), addFirestationRequest.getStation_number());

        firestationService.addFirestation(firestation);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * DELETE http://localhost:8080/firestation/address?address=<address>
     * Supprimer la station de pompiers associée à l'adresse spécifiée.
     * @param address L'adresse de la station de pompiers à supprimer
     * @return ResponseEntity<Void>(HttpStatus.OK)
     * @throws NotFoundException si l'adresse de la station de pompiers n'existe pas
     */

    @Operation(summary = "Delete Firestation by address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firestation successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict: Firestation already exists",
                    content = @Content) })

    @DeleteMapping("/firestation/address")
    public ResponseEntity<Void> deleteFirestationByAddress(
            @Valid @RequestParam ("address")
            @NotBlank(message = "address cannot be null or blank")
            String address) {

        firestationService.deleteFirestationByAddress(address);

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * DELETE http://localhost:8080/firestation/station?station_number=<station_number>
     * Supprimer le ou les stations de pompiers associées au station_number spécifié.
     * @param station_number Numéro du ou des stations de pompiers à supprimer
     * @return ResponseEntity<Void>(HttpStatus.OK)
     * @throws NotFoundException si aucune station de pompiers n'est trouvée pour le numéro de station spécifié
     * @throws ConflictException si la suppression de la station de pompiers échoue en raison d'un conflit
     */

    @Operation(summary = "Delete Firestations by station")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firestation successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request",
                    content = @Content) })

    @DeleteMapping("/firestation/station")
    public ResponseEntity<Void> deleteFirestationByStation(
            @Valid @RequestParam ("station_number")
            @NotNull(message = "station_number cannot be null")
            @Positive(message = "station_number must be positive")
            Integer station_number) {

        firestationService.deleteFirestationByStation(station_number);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}