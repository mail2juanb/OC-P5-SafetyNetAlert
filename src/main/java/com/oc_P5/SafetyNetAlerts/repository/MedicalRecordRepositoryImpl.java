package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {
    private final DataReader dataReaderService;

    @Override
    public List<MedicalRecord> getMedicalRecords() {
        return dataReaderService.getData().getMedicalRecords();
    }

    @Override
    public Optional<MedicalRecord> findMedicalRecordById(String id) {
        return getMedicalRecords()
                .stream()
                .filter(MedicalRecord -> MedicalRecord.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean medicalRecordByIdExists(MedicalRecord medicalRecord) {
        return findMedicalRecordById(medicalRecord.getId()).isPresent();
    }

    @Override
    public void addMedicalRecordMapping(MedicalRecord addMedicalRecord) {
        List<MedicalRecord> medicalRecordList = getMedicalRecords();
        medicalRecordList.add(addMedicalRecord);
    }

    @Override
    //String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
    public Optional<MedicalRecord> updateMedicalRecordMapping(MedicalRecord updateMedicalRecord) {
        return findMedicalRecordById(updateMedicalRecord.getId())
                .map(medicalRecord -> {
                    if(updateMedicalRecord.getBirthdate() != null) {
                        medicalRecord.setBirthdate(updateMedicalRecord.getBirthdate());
                    }
                    if(updateMedicalRecord.getMedications() != null) {
                        medicalRecord.setMedications(updateMedicalRecord.getMedications());
                    }
                    if(updateMedicalRecord.getAllergies() != null) {
                        medicalRecord.setAllergies(updateMedicalRecord.getAllergies());
                    }

                    return medicalRecord;
                });
    }

    @Override
    public void deleteMedicalRecordMapping(MedicalRecord deleteMedicalRecord) {
        List<MedicalRecord> medicalRecordList = getMedicalRecords();
        medicalRecordList.removeIf(medicalRecord -> medicalRecord.getId().equals(deleteMedicalRecord.getId()));
    }

}
