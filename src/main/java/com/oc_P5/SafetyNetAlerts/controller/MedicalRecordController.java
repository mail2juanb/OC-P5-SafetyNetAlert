package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.controller.requests.MedicalRecordRequest;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordServiceImpl medicalRecordService;



    /**
     * POST /medicalRecord
     * Add a medical record
     *
     * @param addMedicalRecordRequest MedicalRecordRequest validated with :
     *                                String address @NotBlank,
     *                                String lastName @NotBlank,
     *                                LocalDate birthdate @Past,
     *                                List<String> medications,
     *                                List<String> allergies
     * @return ResponseEntity<?> (HttpStatus.CREATED)
     * @throws ConflictException if MedicalRecord already exists
     */

    @Operation(summary = "Add a MedicalRecord")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MedicalRecord successfully added", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict: MedicalRecord already exists", content = @Content)
    })

    @PostMapping("/medicalRecord")
    public ResponseEntity<?> addMedicalRecord(@Valid @RequestBody MedicalRecordRequest addMedicalRecordRequest) {

        final MedicalRecord medicalRecord = new MedicalRecord(addMedicalRecordRequest);

        medicalRecordService.addMedicalRecord(medicalRecord);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * PUT /medicalRecord
     * Update an existing medical record (as mentioned above, assume the first and last names remain unchanged)
     * @param updateMedicalRecordRequest MedicalRecordRequest validated with :
     *                                   String address @NotBlank,
     *                                   String lastName @NotBlank,
     *                                   LocalDate birthdate @Past,
     *                                   List<String> medications,
     *                                   List<String> allergies
     * @return ResponseEntity<?> (HttpStatus.OK)
     * @throws NotFoundException if MedicalRecord cannot be found
     */

    @Operation(summary = "Update a MedicalRecord")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalRecord successfully updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request", content = @Content)
    })

    @PutMapping("/medicalRecord")
    public ResponseEntity<?> updateMedicalRecord(@Valid @RequestBody MedicalRecordRequest updateMedicalRecordRequest) {

        final MedicalRecord medicalRecord = new MedicalRecord(updateMedicalRecordRequest);

        medicalRecordService.updateMedicalRecord(medicalRecord);

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * DELETE /medicalRecord
     * Delete a medical record (a combination of first and last name as a unique identifier).
     *
     * @param deleteMedicalRecordRequest MedicalRecordRequest validated with :
     *                                   String address @NotBlank,
     *                                   String lastName @NotBlank,
     *                                   LocalDate birthdate @Past,
     *                                   List<String> medications,
     *                                   List<String> allergies
     * @return ResponseEntity<?> (HttpStatus.OK)
     * @throws NotFoundException if id (firstName-lastName) doesn't exist
     */

    @Operation(summary = "Delete a MedicalRecord")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalRecord successfully deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request", content = @Content)
    })

    @DeleteMapping("/medicalRecord")
    public ResponseEntity<?> deleteMedicalRecord(@Valid @RequestBody MedicalRecordRequest deleteMedicalRecordRequest) {

        final MedicalRecord medicalRecord = new MedicalRecord(deleteMedicalRecordRequest);

        medicalRecordService.deleteMedicalRecord(medicalRecord);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}