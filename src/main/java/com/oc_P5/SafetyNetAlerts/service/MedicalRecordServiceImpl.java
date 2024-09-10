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
        return medicalrecordRepository.getAll();
    }

    public void addMedicalRecordService(MedicalRecord medicalRecord) {
        if(medicalrecordRepository.existsById(medicalRecord.getId())) {
            throw new ConflictException("MedicalRecord already exists");
        }
        medicalrecordRepository.save(medicalRecord);
    }

    public void updateMedicalRecordService(MedicalRecord medicalRecord) {
        if(medicalRecord == null ){
            //todo : use custom exception
            throw new IllegalArgumentException("MedicalRecord can not be null");

        }

        MedicalRecord updatedMedicalRecord = medicalrecordRepository.findById(medicalRecord.getId())
                .orElseThrow(() -> new NotFoundException("MedicalRecord doesn't exist with id = " + medicalRecord.getId()))
                .update(medicalRecord);

        medicalrecordRepository.update(updatedMedicalRecord);
    }

    public void deleteMedicalRecordService(MedicalRecord medicalRecord) {
        if(!medicalrecordRepository.existsById(medicalRecord.getId())) {
            throw new NotFoundException("MedicalRecord doesn't exist");
        }
        medicalrecordRepository.delete(medicalRecord);

    }

}
