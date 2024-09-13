package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
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


    public boolean isMedicalRecordEmpty(MedicalRecord medicalRecord) {
        return (medicalRecord.getFirstName() == null || medicalRecord.getFirstName().isEmpty()) &&
                (medicalRecord.getLastName() == null || medicalRecord.getLastName().isEmpty()) &&
                (medicalRecord.getBirthdate() == null) &&
                (medicalRecord.getMedications() == null || medicalRecord.getMedications().isEmpty())  &&
                (medicalRecord.getAllergies() == null || medicalRecord.getAllergies().isEmpty());
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalrecordRepository.getAll();
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        if(isMedicalRecordEmpty(medicalRecord)) {
            // NOTE Write Throw new Exception : NullOrEmptyObjectException
            throw new NullOrEmptyObjectException("MedicalRecord can not be null or empty");
        }
        if(medicalrecordRepository.existsById(medicalRecord.getId())) {
            throw new ConflictException("MedicalRecord already exists for : " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
        }
        medicalrecordRepository.save(medicalRecord);
    }

    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        if(isMedicalRecordEmpty(medicalRecord)) {
            // NOTE Write Throw new Exception : NullOrEmptyObjectException
            throw new NullOrEmptyObjectException("MedicalRecord can not be null or empty");
        }
        MedicalRecord updatedMedicalRecord = medicalrecordRepository.findById(medicalRecord.getId())
                .orElseThrow(() -> new NotFoundException("MedicalRecord doesn't exist with id = " + medicalRecord.getId()))
                .update(medicalRecord);
        medicalrecordRepository.update(updatedMedicalRecord);
    }

    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        if(isMedicalRecordEmpty(medicalRecord)) {
            // NOTE Write Throw new Exception : NullOrEmptyObjectException
            throw new NullOrEmptyObjectException("MedicalRecord can not be null or empty");
        }
        if(!medicalrecordRepository.existsById(medicalRecord.getId())) {
            throw new NotFoundException("MedicalRecord doesn't exist");
        }
        medicalrecordRepository.delete(medicalRecord);

    }

}
