package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordServiceImpl medicalRecordService;

//    public MedicalRecordController(MedicalRecordServiceImpl medicalRecordService) {
//        this.medicalRecordService = medicalRecordService;
//    }

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
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * PUT http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<Void> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.updateMedicalRecord(medicalRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * DELETE http://localhost:8080/medicalRecord
     * @param medicalRecord un object MedicalRecord contenant : String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.deleteMedicalRecord(medicalRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
