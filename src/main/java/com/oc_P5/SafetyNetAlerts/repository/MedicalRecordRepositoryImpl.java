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
}
