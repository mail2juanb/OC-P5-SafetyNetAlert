package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.controller.requests.MedicalRecordRequest;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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
     * POST http://localhost:8080/medicalRecord
     * @param addMedicalRecordRequest un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<String>(HttpStatus.CREATED)
     * @throws ConflictException si le MedicalRecord existe déjà
     */

    @Operation(summary = "Add a MedicalRecord")
    @PostMapping("/medicalRecord")
    public ResponseEntity<String> addMedicalRecord(@Valid @RequestBody MedicalRecordRequest addMedicalRecordRequest) {

        final MedicalRecord medicalRecord = new MedicalRecord(addMedicalRecordRequest);

        medicalRecordService.addMedicalRecord(medicalRecord);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * PUT http://localhost:8080/medicalRecord
     * @param updateMedicalRecordRequest un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     * @throws NotFoundException si le MedicalRecord est introuvable
     */

    @Operation(summary = "Update a MedicalRecord")
    @PutMapping("/medicalRecord")
    public ResponseEntity<Void> updateMedicalRecord(@Valid @RequestBody MedicalRecordRequest updateMedicalRecordRequest) {

        final MedicalRecord medicalRecord = new MedicalRecord(updateMedicalRecordRequest);

        medicalRecordService.updateMedicalRecord(medicalRecord);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * DELETE http://localhost:8080/medicalRecord
     * @param deleteMedicalRecordRequest un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     * @throws NotFoundException si le MedicalRecord est introuvable
     */

    @Operation(summary = "Delete a MedicalRecord")
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecord(@Valid @RequestBody MedicalRecordRequest deleteMedicalRecordRequest) {

        final MedicalRecord medicalRecord = new MedicalRecord(deleteMedicalRecordRequest);

        medicalRecordService.deleteMedicalRecord(medicalRecord);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}