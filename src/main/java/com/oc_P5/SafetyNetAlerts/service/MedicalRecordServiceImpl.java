package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;



    @Override
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        if(medicalRecordRepository.existsById(medicalRecord.getId())) {
            throw new ConflictException("MedicalRecord already exists for : " + medicalRecord.getId());
        }
        medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        if(!medicalRecordRepository.existsById(medicalRecord.getId())) {
            throw new NotFoundException("MedicalRecord doesn't exist with id = " + medicalRecord.getId());
        }
        medicalRecordRepository.update(medicalRecord);
    }

    @Override
    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordRepository.delete(medicalRecord);
    }

}