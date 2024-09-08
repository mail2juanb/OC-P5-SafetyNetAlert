package com.oc_P5.SafetyNetAlerts.controller;


import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
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
     * @return Liste des medicalRecords ainsi que leurs attributs
     */
    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getMedicalRecordsService() {
        return medicalRecordService.getMedicalRecordsService();
    }


    /**
     * POST http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.CREATED)
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<String> addMedicalRecordMappingController(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecordMappingService(medicalRecord);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * PUT http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<String> updateMedicalRecordMappingController(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.updateMedicalRecordMappingService(medicalRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * DELETE http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return null
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<String> deleteMedicalRecordMappingController(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.deleteMedicalRecordMappingService(medicalRecord);
        // FIXME moi je dirais plutot ca, non ? return new ResponseEntity<>(HttpStatus.OK);
        return null;
    }

}
