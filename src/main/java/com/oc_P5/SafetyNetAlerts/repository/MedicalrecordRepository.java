package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Medicalrecord;

import java.util.List;
import java.util.Optional;

public interface MedicalrecordRepository {
    List<Medicalrecord> getMedicalrecords();

    Optional<Medicalrecord> findMedicalrecordById(String id);
}
