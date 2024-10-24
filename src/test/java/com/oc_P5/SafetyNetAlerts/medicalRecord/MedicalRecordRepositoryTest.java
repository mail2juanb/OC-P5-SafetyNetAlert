package com.oc_P5.SafetyNetAlerts.medicalRecord;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepositoryImpl;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private MedicalRecordRepositoryImpl medicalRecordRepository;

    private List<MedicalRecord> medicalRecordListMock;


    @BeforeEach
    public void setUp() {

        // NOTE Test data creation
        LocalDate birthdate1 = LocalDate.parse("09/01/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
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

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setMedicalRecords(medicalRecordListMock);

        when(dataReaderService.getData()).thenReturn(dataWrapperList);
    }

    @Test
    void getAll_shouldReturnListOfMedicalRecords() {
        // When the method is called on the repository
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.getAll();

        // Then verify that the returned list contains the expected values
        assertEquals(2, medicalRecordList.size());
        assertEquals("firstNameTest1", medicalRecordList.get(0).getFirstName());
        assertEquals("firstNameTest2", medicalRecordList.get(1).getFirstName());
    }

    @Test
    void saveMedicalRecord_shouldSave() {
        // Given a new MedicalRecord to be added
        LocalDate birthdate = LocalDate.parse("09/01/1999", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList = List.of("medicationTestAdd1:999mg", "medicationTestAdd2:999mg");
        List<String> allergiesList = List.of("allergieTestAdd1", "allergieTestAdd2");
        MedicalRecord medicalRecord = new MedicalRecord("firstNameTest3", "lastNameTest3", birthdate, medicationList, allergiesList);

        // When the record is saved using the repository
        medicalRecordRepository.save(medicalRecord);

        // Then verify that the record is correctly added to the list
        assertEquals(3, medicalRecordListMock.size());
        assertEquals("firstNameTest1", medicalRecordListMock.get(0).getFirstName());
        assertEquals("firstNameTest2", medicalRecordListMock.get(1).getFirstName());
        assertEquals("firstNameTest3", medicalRecordListMock.get(2).getFirstName());
        assertEquals(birthdate, medicalRecordListMock.get(2).getBirthdate());
        assertEquals(medicationList, medicalRecordListMock.get(2).getMedications());
        assertEquals(allergiesList, medicalRecordListMock.get(2).getAllergies());
    }


    @Test
    void updateMedicalRecord_shouldUpdate() {
        // Given an existing MedicalRecord with updated details
        LocalDate birthdate = LocalDate.parse("09/01/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList = List.of("medicationTestUpdate1:999mg", "medicationTestUpdate2:999mg");
        List<String> allergiesList = List.of("allergieTestUpdate1", "allergieTestUpdate2");

        MedicalRecord medicalRecord = new MedicalRecord("firstNameTest1", "lastNameTest1", birthdate, medicationList, allergiesList);

        // When the record is updated in the repository
        medicalRecordRepository.update(medicalRecord);

        // Then verify that the record has been updated in the list
        assertEquals(2, medicalRecordListMock.size());
        assertEquals(medicalRecord.getFirstName(), medicalRecordListMock.getFirst().getFirstName());
        assertEquals(medicalRecord.getLastName(), medicalRecordListMock.getFirst().getLastName());
        assertEquals(medicalRecord.getBirthdate(), medicalRecordListMock.getFirst().getBirthdate());
        assertEquals(medicalRecord.getMedications(), medicalRecordListMock.getFirst().getMedications());
        assertEquals(medicalRecord.getAllergies(), medicalRecordListMock.getFirst().getAllergies());

    }

    @Test
    void deleteMedicalRecord_shouldDelete() {
        // Given a MedicalRecord to be deleted
        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();

        // When the record is deleted from the repository
        medicalRecordRepository.delete(medicalRecord);

        // Then verify that the record is removed from the list
        assertEquals(1, medicalRecordListMock.size());
        assertFalse(medicalRecordListMock.contains(medicalRecord));
    }

    @Test
    void existsById_shouldReturnBooleanWithIdExists(){
        // Given an existing MedicalRecord id
        String id = medicalRecordListMock.getFirst().getId();

        // When the repository checks if the id exists
        boolean exists = medicalRecordRepository.existsById(id);

        // Then verify that the method returns true
        assertTrue(exists);
    }

    @Test
    void existsById_shouldReturnBooleanWithIdNotExists(){
        // Given a non-existing MedicalRecord id
        String id = "idNotExists";

        // When the repository checks if the id exists
        boolean exists = medicalRecordRepository.existsById(id);

        // Then verify that the method returns false
        assertFalse(exists);
    }

}