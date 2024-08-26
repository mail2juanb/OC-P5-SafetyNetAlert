package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Medicalrecord;
import com.oc_P5.SafetyNetAlerts.model.Medicalrecord;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MedicalrecordRepositoryImpl implements MedicalrecordRepository {
    private final DataReader dataReaderService;

    @Override
    public List<Medicalrecord> getMedicalrecords() {
        return dataReaderService.getData().getMedicalRecords();
    }

    @Override
    public Optional<Medicalrecord> findMedicalrecordById(String id) {
        return getMedicalrecords()
                .stream()
                .filter(Medicalrecord -> Medicalrecord.getId().equals(id))
                .findFirst();
    }
}
