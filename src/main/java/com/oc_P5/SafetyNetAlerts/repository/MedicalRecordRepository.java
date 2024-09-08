package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository {

    List<MedicalRecord> getMedicalRecords();

    Optional<MedicalRecord> findMedicalRecordById(String id);

    boolean medicalRecordByIdExists(MedicalRecord medicalRecord);

    void addMedicalRecordMapping(MedicalRecord addMedicalRecord);

    Optional<MedicalRecord> updateMedicalRecordMapping(MedicalRecord updateMedicalRecord);

    void deleteMedicalRecordMapping (MedicalRecord deleteMedicalRecord);

}
