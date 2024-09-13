package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class MedicalRecordController {

    private final MedicalRecordServiceImpl medicalRecordService;

    public MedicalRecordController(MedicalRecordServiceImpl medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * GET http://localhost:8080/medicalRecords
     * @return List<MedicalRecord> Liste des medicalRecords ainsi que leurs attributs
     */
    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecordService.getMedicalRecords();
    }


    /**
     * POST http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.CREATED)
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<String> addMedicalRecordController(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * PUT http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<Void> updateMedicalRecordController(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.updateMedicalRecord(medicalRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * DELETE http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecordController(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.deleteMedicalRecord(medicalRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
