package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Création des données de test
        LocalDate birthdate1 = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = Collections.emptyList();
        List<String> allergiesList1 = Collections.emptyList();

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("firstNameTest1");
        medicalRecord1.setLastName("lastNameTest1");
        medicalRecord1.setBirthdate(birthdate1);
        medicalRecord1.setMedications(medicationList1);
        medicalRecord1.setAllergies(allergiesList1);

        LocalDate birthdate2 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList2 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("firstNameTest2");
        medicalRecord2.setLastName("lastNameTest2");
        medicalRecord2.setBirthdate(birthdate2);
        medicalRecord2.setMedications(medicationList2);
        medicalRecord2.setAllergies(allergiesList2);

        medicalRecordListMock = new ArrayList<>();
        medicalRecordListMock.add(medicalRecord1);
        medicalRecordListMock.add(medicalRecord2);
    }

    @Test
    // On va vérifier ici que la méthode renvoi la liste des MedicalRecord
    void getMedicalRecords_shouldReturnListOfMedicalRecord() {
        // Given
        when(medicalRecordRepository.getAll()).thenReturn(medicalRecordListMock);

        // When
        List<MedicalRecord> result = medicalRecordService.getMedicalRecords();

        // Then
        assertEquals(medicalRecordListMock, result);
        verify(medicalRecordRepository, times(1)).getAll();
    }

    @Test
    // On va vérifier ici que lorsqu'un MedicalRecord est ajoutée, il est correctement sauvegardée avec les bons attributs.
    void addMedicalRecord_shouldAddMedicalRecordWhenNotExists() {
        // Given
        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();
        medicalRecord.setFirstName("firstNameNew");
        medicalRecord.setLastName("lastNameNew");

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(false);

        // When
        medicalRecordService.addMedicalRecord(medicalRecord);

        // Then
        verify(medicalRecordRepository, times(1)).existsById(medicalRecord.getId());
        verify(medicalRecordRepository, times(1)).save(medicalRecord);

        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).save(medicalRecordArgumentCaptor.capture());

        MedicalRecord savedMedicalRecord = medicalRecordArgumentCaptor.getValue();
        assertThat(savedMedicalRecord).isEqualTo(medicalRecord);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecord")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException
    void addMedicalRecord_shouldReturnNullOrEmptyObjectExceptionWithEmptyMedicalRecord(MedicalRecord medicalRecord) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> medicalRecordService.addMedicalRecord(medicalRecord));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
        verify(medicalRecordRepository, never()).existsById(anyString());
        verify(medicalRecordRepository, never()).save(medicalRecord);
    }

    @Test
    // On va vérifier ici que la méthode lève une ConflictException lorsque le medicalRecord existe déjà
    void addMedicalRecord_shouldReturnConflictExceptionWhenExists() {
        // Given
        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(true);

        // When / Then
        ConflictException thrown = assertThrows(ConflictException.class, () -> medicalRecordService.addMedicalRecord(medicalRecord));
        assertThat(thrown.getMessage()).contains(medicalRecord.getFirstName());
        assertThat(thrown.getMessage()).contains(medicalRecord.getLastName());

        verify(medicalRecordRepository, times(1)).existsById("firstNameTest1-lastNameTest1");
        verify(medicalRecordRepository, never()).save(medicalRecord);
    }

    @Test
    // On va vérifier ici que le MedicalRecord est mis à jour avec un MedicalRecord valide
    void updateMedicalRecord_shouldUpdateMedicalRecordWhenExists() {
        // Given
        LocalDate birthdate = LocalDate.parse("09/01/1995", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList = List.of("medicationTest1:999mg", "medicationTest2:299mg");
        List<String> allergiesList = Collections.emptyList();

        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setMedications(medicationList);
        medicalRecord.setAllergies(allergiesList);

        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.of(medicalRecordListMock.getFirst()));

        // When
        medicalRecordService.updateMedicalRecord(medicalRecord);

        // Then
        verify(medicalRecordRepository, times(1)).findById(medicalRecord.getId());
        verify(medicalRecordRepository, times(1)).update(medicalRecordListMock.getFirst());

        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).update(medicalRecordArgumentCaptor.capture());
        MedicalRecord updatedMedicalRecord = medicalRecordArgumentCaptor.getValue();
        assertThat(updatedMedicalRecord).isEqualTo(medicalRecord);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecord")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException
    void updateMedicalRecord_shouldReturnNullOrEmptyObjectExceptionWithEmptyMedicalRecord(MedicalRecord medicalRecord) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> medicalRecordService.updateMedicalRecord(medicalRecord));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
        verify(medicalRecordRepository, never()).findById(anyString());
        verify(medicalRecordRepository, never()).update(medicalRecord);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque le medicalRecord n'existe pas
    void updateMedicalRecord_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given
        MedicalRecord medicalRecord = medicalRecordListMock.get(1);
        medicalRecord.setFirstName("unknownFirstName");
        medicalRecord.setLastName("unknownLastName");

        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.empty());

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> medicalRecordService.updateMedicalRecord(medicalRecord));
        assertThat(thrown.getMessage()).contains(medicalRecord.getId());

        verify(medicalRecordRepository, times(1)).findById(medicalRecord.getId());
        verify(medicalRecordRepository, never()).update(any(MedicalRecord.class));
    }

    @Test
    // On va vérifier ici que le MedicalRecord est supprimé avec un MedicalRecord valide
    void deleteMedicalRecord_shouldDeleteMedicalRecord() {
        // Given
        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(true);

        // When
        medicalRecordService.deleteMedicalRecord(medicalRecord);

        // Then
        verify(medicalRecordRepository, times(1)).existsById(medicalRecord.getId());
        verify(medicalRecordRepository, times(1)).delete(medicalRecord);

        ArgumentCaptor<MedicalRecord> medicalRecordArgumentCaptor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).delete(medicalRecordArgumentCaptor.capture());
        MedicalRecord deletedMedicalRecord = medicalRecordArgumentCaptor.getValue();
        assertThat(deletedMedicalRecord).isEqualTo(medicalRecord);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecord")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException
    void deleteMedicalRecord_shouldReturnNullOrEmptyObjectExceptionWithEmptyMedicalRecord(MedicalRecord medicalRecord) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> medicalRecordService.deleteMedicalRecord(medicalRecord));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
        verify(medicalRecordRepository, never()).existsById(anyString());
        verify(medicalRecordRepository, never()).delete(medicalRecord);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque le medicalRecord n'existe pas
    void deleteMedicalRecord_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given
        MedicalRecord medicalRecord = medicalRecordListMock.get(1);
        medicalRecord.setFirstName("unknownFirstName");
        medicalRecord.setLastName("unknownLastName");

        when(medicalRecordRepository.existsById(medicalRecord.getId())).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> medicalRecordService.deleteMedicalRecord(medicalRecord));
        assertThat(thrown.getMessage()).contains(medicalRecord.getId());

        verify(medicalRecordRepository, times(1)).existsById(medicalRecord.getId());
        verify(medicalRecordRepository, never()).delete(any(MedicalRecord.class));
    }



    // Fournit des valeurs de MedicalRecord, y compris null
    static Stream<MedicalRecord> provideInvalidMedicalRecord() {
        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName(null);
        medicalRecord1.setLastName(null);
        medicalRecord1.setBirthdate(null);
        medicalRecord1.setMedications(null);
        medicalRecord1.setAllergies(null);

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("");
        medicalRecord2.setLastName("");
        medicalRecord2.setBirthdate(null);
        medicalRecord2.setMedications(List.of("", " "));
        medicalRecord2.setAllergies(List.of("", " "));

        return Stream.of(medicalRecord1, medicalRecord2);
    }

}