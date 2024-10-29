package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.controller.requests.PersonRequest;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.PersonServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonServiceImpl personService;



    /**
     * POST /person
     * Add a Person
     *
     * @param addPersonRequest MedicalRecordRequest validated with :
     *                         String firstName @NotBlank
     *                         String lastName @NotBlank
     *                         String address
     *                         String city
     *                         Integer zip @Positive
     *                         String phone
     *                         String email @Email
     * @return ResponseEntity<?> (HttpStatus.CREATED)
     * @throws ConflictException if the Person already exists
     */

    @Operation(summary = "Add a Person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person successfully added", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict: Person already exists", content = @Content)
    })

    @PostMapping("/person")
    public ResponseEntity<?> addPerson(@Valid @RequestBody PersonRequest addPersonRequest) {

        final Person person = new Person(addPersonRequest);

        personService.addPerson(person);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * PUT /person
     * Update an existing person (for the moment, let's assume that the first and last name don't change,
     * but other fields can be can be modified)
     *
     * @param updatePersonRequest MedicalRecordRequest validated with :
     *                            String firstName @NotBlank
     *                            String lastName @NotBlank
     *                            String address
     *                            String city
     *                            Integer zip @Positive
     *                            String phone
     *                            String email @Email
     * @return ResponseEntity<?> (HttpStatus.OK)
     * @throws NotFoundException si la personne est introuvable
     */

    @Operation(summary = "Update a Person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person successfully updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request", content = @Content)
    })

    @PutMapping("/person")
    public ResponseEntity<?> updatePerson(@Valid @RequestBody PersonRequest updatePersonRequest) {

        final Person person = new Person(updatePersonRequest);

        personService.updatePerson(person);

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * DELETE /person
     * Delete a person (use a combination of first and last name as a unique identifier)
     *
     * @param deletePersonRequest MedicalRecordRequest validated with :
     *                            String firstName @NotBlank
     *                            String lastName @NotBlank
     *                            String address
     *                            String city
     *                            Integer zip @Positive
     *                            String phone
     *                            String email @Email
     * @return ResponseEntity<?> (HttpStatus.OK)
     */

    @Operation(summary = "Delete a Person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person successfully deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @DeleteMapping("/person")
    public ResponseEntity<?> deleteFirestation(@Valid @RequestBody PersonRequest deletePersonRequest) {

        final Person person = new Person(deletePersonRequest);

        personService.deletePerson(person);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}