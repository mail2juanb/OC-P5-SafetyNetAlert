package com.oc_P5.SafetyNetAlerts.medicalRecord;

import com.oc_P5.SafetyNetAlerts.controller.MedicalRecordController;
import com.oc_P5.SafetyNetAlerts.controller.requests.MedicalRecordRequest;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordServiceImpl medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;



    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void addMedicalRecord_shouldReturnResponseEntity() {
        // Given MedicalRecord to add
        final LocalDate birthdate = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        final List<String> medicationList = Collections.emptyList();
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest("addFirstName", "addLastName", birthdate, medicationList, allergiesList);
        final MedicalRecord medicalRecordExpect = new MedicalRecord("addFirstName", "addLastName", birthdate, medicationList, allergiesList);

        doNothing().when(medicalRecordService).addMedicalRecord(any());

        // When MedicalRecord is post
        ResponseEntity<String> response = medicalRecordController.addMedicalRecord(medicalRecordRequest);

        // Then MedicalRecord is sent to service with correct values and check HttpStatus.CREATED
        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordService).addMedicalRecord(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue().getFirstName()).isEqualTo(medicalRecordExpect.getFirstName());
        assertThat(medicalRecordArgumentCaptor.getValue().getLastName()).isEqualTo(medicalRecordExpect.getLastName());
        assertThat(medicalRecordArgumentCaptor.getValue().getBirthdate()).isEqualTo(medicalRecordExpect.getBirthdate());
        assertThat(medicalRecordArgumentCaptor.getValue().getMedications()).isEqualTo(medicalRecordExpect.getMedications());
        assertThat(medicalRecordArgumentCaptor.getValue().getAllergies()).isEqualTo(medicalRecordExpect.getAllergies());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void updateMedicalRecord_shouldReturnResponseEntity() {
        // Given a MedicalRecord to update
        final LocalDate birthdate = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        final List<String> medicationList = Collections.emptyList();
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest("updateFirstName", "updateLastName", birthdate, medicationList, allergiesList);
        final MedicalRecord expectedMedicalRecord = new MedicalRecord("updateFirstName", "updateLastName", birthdate, medicationList, allergiesList);

        doNothing().when(medicalRecordService).updateMedicalRecord(any());

        // When MedicalRecord is updated
        ResponseEntity<Void> response = medicalRecordController.updateMedicalRecord(medicalRecordRequest);

        // Then MedicalRecord is sent to the service and check HttpStatus.OK
        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordService).updateMedicalRecord(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue().getFirstName()).isEqualTo(expectedMedicalRecord.getFirstName());
        assertThat(medicalRecordArgumentCaptor.getValue().getLastName()).isEqualTo(expectedMedicalRecord.getLastName());
        assertThat(medicalRecordArgumentCaptor.getValue().getBirthdate()).isEqualTo(expectedMedicalRecord.getBirthdate());
        assertThat(medicalRecordArgumentCaptor.getValue().getMedications()).isEqualTo(expectedMedicalRecord.getMedications());
        assertThat(medicalRecordArgumentCaptor.getValue().getAllergies()).isEqualTo(expectedMedicalRecord.getAllergies());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void deleteMedicalRecord_shouldReturnResponseEntity() {
        // Given a MedicalRecord to delete
        final LocalDate birthdate = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest deleteMedicalRecordRequest = new MedicalRecordRequest("firstName", "lastName", birthdate, medicationList, allergiesList);
        final MedicalRecord expectedMedicalRecord = new MedicalRecord("firstName", "lastName", birthdate, medicationList, allergiesList);

        doNothing().when(medicalRecordService).deleteMedicalRecord(any());

        // When MedicalRecord is deleted
        ResponseEntity<Void> response = medicalRecordController.deleteMedicalRecord(deleteMedicalRecordRequest);

        // Then MedicalRecord is sent to the service and check HttpStatus.OK
        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordService).deleteMedicalRecord(medicalRecordArgumentCaptor.capture());
        assertThat(medicalRecordArgumentCaptor.getValue().getFirstName()).isEqualTo(expectedMedicalRecord.getFirstName());
        assertThat(medicalRecordArgumentCaptor.getValue().getLastName()).isEqualTo(expectedMedicalRecord.getLastName());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
