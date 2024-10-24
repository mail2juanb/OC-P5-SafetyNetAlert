package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.service.PersonsInfoLastNameServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class PersonsInfoLastNameController {

    private final PersonsInfoLastNameServiceImpl personsInfoLastNameService;


    /**
     * GET /personInfoLastName
     * This url must return the name, address, age, e-mail address and medical history
     * (medication, dosage and allergies) of each resident.
     * If several people have the same name, they must all appear.
     *
     * @param lastName String lastName validated with @NotBlank
     * @return ResponseEntity<List<PersonInfoLastName>> (HttpStatus.OK)
     * @throws NotFoundException if lastName cannot be found in Person or MedicalRecord repository
     */

    @Operation(summary = "Retrieve persons infos with lastName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persons infos successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonInfoLastName[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request", content = @Content)
    })

    @GetMapping("/personInfoLastName")
    public ResponseEntity<List<PersonInfoLastName>> getPersonsInfoLastName(
            @Valid @RequestParam("lastName")
            @NotBlank(message = "lastName cannot be blank")
            String lastName) {

        List<PersonInfoLastName> personInfoLastName = personsInfoLastNameService.getPersonsInfoLastName(lastName);

        return new ResponseEntity<>(personInfoLastName, HttpStatus.OK);
    }

}
