package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordServiceImpl medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;



    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getMedicalRecords_shouldReturnListOfMedicalRecord() {
        // Given
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        when(medicalRecordService.getMedicalRecords()).thenReturn(medicalRecordList);

        // When
        List<MedicalRecord> result = medicalRecordController.getMedicalRecords();

        // Then
        verify(medicalRecordService, times(1)).getMedicalRecords();
        assertEquals(medicalRecordList, result);
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void addMedicalRecordController_shouldReturnResponseEntity() {
        // Given
        MedicalRecord medicalRecord = new MedicalRecord();
        doNothing().when(medicalRecordService).addMedicalRecord(medicalRecord);

        // When
        ResponseEntity<String> response = medicalRecordController.addMedicalRecordController(medicalRecord);

        // Then
        verify(medicalRecordService, times(1)).addMedicalRecord(medicalRecord);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void updateMedicalRecordController_shouldReturnResponseEntity() {
        // Given
        MedicalRecord medicalRecord = new MedicalRecord();
        doNothing().when(medicalRecordService).updateMedicalRecord(medicalRecord);

        // When
        ResponseEntity<Void> response = medicalRecordController.updateMedicalRecordController(medicalRecord);

        // Then
        verify(medicalRecordService, times(1)).updateMedicalRecord(medicalRecord);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void deleteMedicalRecordController_shouldReturnResponseEntity() {
        // Given
        MedicalRecord medicalRecord = new MedicalRecord();
        doNothing().when(medicalRecordService).deleteMedicalRecord(medicalRecord);

        // When
        ResponseEntity<Void> response = medicalRecordController.deleteMedicalRecordController(medicalRecord);

        // Then
        verify(medicalRecordService, times(1)).deleteMedicalRecord(medicalRecord);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
