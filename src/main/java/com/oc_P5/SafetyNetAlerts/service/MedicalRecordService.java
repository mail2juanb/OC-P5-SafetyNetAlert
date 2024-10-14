package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {

    void addMedicalRecord(MedicalRecord medicalRecord);

    void updateMedicalRecord(MedicalRecord medicalRecord);

    void deleteMedicalRecord(MedicalRecord medicalRecord);

}
