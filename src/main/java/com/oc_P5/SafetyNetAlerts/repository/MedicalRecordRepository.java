package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository {

    List<MedicalRecord> getAll();

    Optional<MedicalRecord> findById(String id);

    boolean existsById(String id);

    void save(MedicalRecord addMedicalRecord);

    void update(MedicalRecord updateMedicalRecord);

    void delete(MedicalRecord deleteMedicalRecord);

}
