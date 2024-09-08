package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalrecordRepository;

    public List<MedicalRecord> getMedicalRecordsService() {
        return medicalrecordRepository.getMedicalRecords();
    }

    public void addMedicalRecordMappingService(MedicalRecord medicalRecord) {
        if(medicalrecordRepository.medicalRecordByIdExists(medicalRecord)) {
            throw new ConflictException("MedicalRecord already exists");
        }
        medicalrecordRepository.addMedicalRecordMapping(medicalRecord);
    }

    public void updateMedicalRecordMappingService(MedicalRecord medicalRecord) {
        if(!medicalrecordRepository.medicalRecordByIdExists(medicalRecord)) {
            throw new NotFoundException("MedicalRecord doesn't exist");
        }
        medicalrecordRepository.updateMedicalRecordMapping(medicalRecord);
    }

    public void deleteMedicalRecordMappingService(MedicalRecord medicalRecord) {
        if(!medicalrecordRepository.medicalRecordByIdExists(medicalRecord)) {
            throw new NotFoundException("MedicalRecord doesn't exist");
        }
        medicalrecordRepository.deleteMedicalRecordMapping(medicalRecord);

    }

}
