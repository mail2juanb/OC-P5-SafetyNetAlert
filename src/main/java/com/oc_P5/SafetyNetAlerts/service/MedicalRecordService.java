package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {

    List<MedicalRecord> getMedicalRecordsService();

    void addMedicalRecordService(MedicalRecord medicalRecord);

    void updateMedicalRecordService(MedicalRecord medicalRecord);

    void deleteMedicalRecordService(MedicalRecord medicalRecord);

}
