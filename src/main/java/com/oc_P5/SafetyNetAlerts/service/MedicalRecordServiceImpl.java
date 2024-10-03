package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;


    @Override
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecordRepository.getAll();
    }

    @Override
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        if(isMedicalRecordEmpty(medicalRecord)) {
            throw new NullOrEmptyObjectException("MedicalRecord can not be null or empty");
        }
        if(medicalRecordRepository.existsById(medicalRecord.getId())) {
            throw new ConflictException("MedicalRecord already exists for : " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
        }
        medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        if(isMedicalRecordEmpty(medicalRecord)) {
            throw new NullOrEmptyObjectException("MedicalRecord can not be null or empty");
        }
        MedicalRecord updatedMedicalRecord = medicalRecordRepository.findById(medicalRecord.getId())
                .orElseThrow(() -> new NotFoundException("MedicalRecord doesn't exist with id = " + medicalRecord.getId()))
                .update(medicalRecord);
        medicalRecordRepository.update(updatedMedicalRecord);
    }

    @Override
    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        if(isMedicalRecordEmpty(medicalRecord)) {
            throw new NullOrEmptyObjectException("MedicalRecord can not be null or empty");
        }
        if(!medicalRecordRepository.existsById(medicalRecord.getId())) {
            throw new NotFoundException("MedicalRecord doesn't exist with id : " + medicalRecord.getId());
        }
        medicalRecordRepository.delete(medicalRecord);
    }

    private boolean isMedicalRecordEmpty(MedicalRecord medicalRecord) {
        // Vérifier si les champs sont vides ou null
        boolean isFirstNameEmpty = StringUtils.isBlank(medicalRecord.getFirstName());
        boolean isLastNameEmpty = StringUtils.isBlank(medicalRecord.getLastName());
        boolean isBirthdateNull = (medicalRecord.getBirthdate() == null);
        boolean areMedicationsEmpty = (medicalRecord.getMedications() == null ||
                medicalRecord.getMedications().stream().allMatch(StringUtils::isBlank));
        boolean areAllergiesEmpty = (medicalRecord.getAllergies() == null ||
                medicalRecord.getAllergies().stream().allMatch(StringUtils::isBlank));

        // Retourner vrai si tous les champs sont vides
        return isFirstNameEmpty && isLastNameEmpty && isBirthdateNull && areMedicationsEmpty && areAllergiesEmpty;
    }

}
