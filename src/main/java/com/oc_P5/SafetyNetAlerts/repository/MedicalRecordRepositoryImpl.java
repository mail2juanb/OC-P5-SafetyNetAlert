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
    public List<MedicalRecord> getAll() {
        return dataReaderService.getData().getMedicalRecords();
    }

    @Override
    public Optional<MedicalRecord> findById(String id) {
        return getAll()
                .stream()
                .filter(MedicalRecord -> MedicalRecord.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public void save(MedicalRecord addMedicalRecord) {
        List<MedicalRecord> medicalRecordList = getAll();
        medicalRecordList.add(addMedicalRecord);
    }

    @Override
    //String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
    public void update(MedicalRecord updateMedicalRecord) {
        MedicalRecord medicalRecordToUpdate = findById(updateMedicalRecord.getId()).orElseThrow();
        int index = getAll().indexOf(medicalRecordToUpdate);
        getAll().set(index, updateMedicalRecord );
    }

    @Override
    public void delete(MedicalRecord deleteMedicalRecord) {
        List<MedicalRecord> medicalRecordList = getAll();
        medicalRecordList.removeIf(medicalRecord -> medicalRecord.getId().equals(deleteMedicalRecord.getId()));
    }

}
