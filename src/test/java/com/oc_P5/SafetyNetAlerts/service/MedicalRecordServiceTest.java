package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    MedicalRecordServiceImpl medicalRecordService;

    private List<MedicalRecord> medicalRecordListMock;

    @BeforeEach
    public void setUp() {
        // Test data creation
        LocalDate birthdate1 = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = Collections.emptyList();
        List<String> allergiesList1 = Collections.emptyList();
        MedicalRecord medicalRecord1 = new MedicalRecord("firstNameTest1", "lastNameTest1", birthdate1, medicationList1, allergiesList1);

        LocalDate birthdate2 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList2 = List.of("allergieTest1", "allergieTest2");
        MedicalRecord medicalRecord2 = new MedicalRecord("firstNameTest2", "lastNameTest2", birthdate2, medicationList2, allergiesList2);

        medicalRecordListMock = new ArrayList<>();
        medicalRecordListMock.add(medicalRecord1);
        medicalRecordListMock.add(medicalRecord2);
    }


    @Test
    // On va vérifier ici que lorsqu'un MedicalRecord est ajoutée, il est correctement sauvegardée avec les bons attributs.
    void addMedicalRecord_shouldAddMedicalRecordWhenNotExists() {
        // Given a MedicalRecord to add
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("firstNameNew");
        medicalRecord.setLastName("lastNameNew");

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(false);

        // When MedicalRecord is sent to be added
        medicalRecordService.addMedicalRecord(medicalRecord);

        // Then the repository should check if the MedicalRecord already exists, and save the new record
        verify(medicalRecordRepository, times(1)).existsById(medicalRecord.getId());
        verify(medicalRecordRepository, times(1)).save(medicalRecord);

        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).save(medicalRecordArgumentCaptor.capture());

        MedicalRecord savedMedicalRecord = medicalRecordArgumentCaptor.getValue();
        assertThat(savedMedicalRecord).isEqualTo(medicalRecord);
    }

    @Test
    // On va vérifier ici que la méthode lève une ConflictException lorsque le medicalRecord existe déjà
    void addMedicalRecord_shouldReturnConflictExceptionWhenExists() {
        // Given an existing MedicalRecord
        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(true);

        // When trying to add the existing MedicalRecord
        ConflictException thrown = assertThrows(ConflictException.class, () -> medicalRecordService.addMedicalRecord(medicalRecord));

        // Then a ConflictException should be thrown and no save should occur
        assertThat(thrown.getMessage()).contains(medicalRecord.getFirstName(), medicalRecord.getLastName());

        verify(medicalRecordRepository, times(1)).existsById("firstNameTest1-lastNameTest1");
        verify(medicalRecordRepository, never()).save(medicalRecord);
    }

    @Test
    // On va vérifier ici que le MedicalRecord est mis à jour avec un MedicalRecord valide
    void updateMedicalRecord_shouldUpdateMedicalRecordWhenExists() {
        // Given an existing MedicalRecord with updated details
        LocalDate birthdate = LocalDate.parse("09/01/1995", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList = List.of("medicationTest1:999mg", "medicationTest2:299mg");
        List<String> allergiesList = Collections.emptyList();

        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setMedications(medicationList);
        medicalRecord.setAllergies(allergiesList);

        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.of(medicalRecordListMock.getFirst()));

        // When MedicalRecord is updated
        medicalRecordService.updateMedicalRecord(medicalRecord);

        // Then the repository check if the MedicalRecord exists and update the MedicalRecord
        verify(medicalRecordRepository, times(1)).findById(medicalRecord.getId());
        verify(medicalRecordRepository, times(1)).update(medicalRecordListMock.getFirst());

        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).update(medicalRecordArgumentCaptor.capture());
        MedicalRecord updatedMedicalRecord = medicalRecordArgumentCaptor.getValue();
        assertThat(updatedMedicalRecord).isEqualTo(medicalRecord);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque le medicalRecord n'existe pas
    void updateMedicalRecord_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given a MedicalRecord that doesn't exist
        MedicalRecord medicalRecord = medicalRecordListMock.get(1);
        medicalRecord.setFirstName("unknownFirstName");
        medicalRecord.setLastName("unknownLastName");

        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.empty());

        // When trying to update the MedicalRecord
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> medicalRecordService.updateMedicalRecord(medicalRecord));

        // Then a NotFoundException should be thrown and no update should occur
        assertThat(thrown.getMessage()).contains(medicalRecord.getId());

        verify(medicalRecordRepository, times(1)).findById(medicalRecord.getId());
        verify(medicalRecordRepository, never()).update(any(MedicalRecord.class));
    }

    @Test
    // On va vérifier ici que le MedicalRecord est supprimé avec un MedicalRecord valide
    void deleteMedicalRecord_shouldDeleteMedicalRecord() {
        // Given an existing MedicalRecord to be deleted
        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(true);

        // When the MedicalRecord is deleted
        medicalRecordService.deleteMedicalRecord(medicalRecord);

        // Then the repository should delete the MedicalRecord
        verify(medicalRecordRepository, times(1)).existsById(medicalRecord.getId());
        verify(medicalRecordRepository, times(1)).delete(medicalRecord);

        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).delete(medicalRecordArgumentCaptor.capture());
        MedicalRecord deletedMedicalRecord = medicalRecordArgumentCaptor.getValue();
        assertThat(deletedMedicalRecord).isEqualTo(medicalRecord);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque le medicalRecord n'existe pas
    void deleteMedicalRecord_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given a MedicalRecord that doesn't exist
        MedicalRecord medicalRecord = medicalRecordListMock.get(1);
        medicalRecord.setFirstName("unknownFirstName");
        medicalRecord.setLastName("unknownLastName");

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(false);

        // When trying to delete the MedicalRecord
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> medicalRecordService.deleteMedicalRecord(medicalRecord));

        // Then a NotFoundException should be thrown and no delete should occur
        assertThat(thrown.getMessage()).contains(medicalRecord.getId());

        verify(medicalRecordRepository, times(1)).existsById(medicalRecord.getId());
        verify(medicalRecordRepository, never()).delete(any(MedicalRecord.class));
    }

}