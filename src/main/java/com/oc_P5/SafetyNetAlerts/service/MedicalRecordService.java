package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {

    List<MedicalRecord> getMedicalRecordsService();

    void addMedicalRecordMappingService(MedicalRecord medicalRecord);

    void updateMedicalRecordMappingService(MedicalRecord medicalRecord);

    void deleteMedicalRecordMappingService(MedicalRecord medicalRecord);

}
