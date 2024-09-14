package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
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


//@ExtendWith(SpringExtension.class)
// NOTE j'ai remis MockitoExtension ça marche aussi. J'ai pas compris pourquoi tu avais changé
@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private MedicalRecordRepositoryImpl medicalRecordRepository;

    private List<MedicalRecord> medicalRecordListMock;


    @BeforeEach
    public void setUp() {
        // Initialisation des mocks fait par l'annotation @ExtendWith(SpringExtension.class)
        // Creation des données de test - String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
        LocalDate birthdate1 = LocalDate.parse("09/01/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
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

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setMedicalRecords(medicalRecordListMock);

        // Configure le mock pour MedicalRecordRepository
        when(dataReaderService.getData()).thenReturn(dataWrapperList);
    }

    @Test
    // On va vérifier ici que la méthode retourne bien les données mock
    void getMedicalRecord_shouldReturnListOfMedicalRecords() {
        // When
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.getAll();

        // Then
        assertEquals(2, medicalRecordList.size());
        assertEquals("firstNameTest1", medicalRecordList.get(0).getFirstName());
        assertEquals("firstNameTest2", medicalRecordList.get(1).getFirstName());
    }

    @Test
    // On va vérifier ici que la méthode ajoute correctement un MedicalRecord
    void saveMedicalRecord_shouldSave() {
        // Given
        LocalDate birthdate = LocalDate.parse("09/01/1999", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList = List.of("medicationTestAdd1:999mg", "medicationTestAdd2:999mg");
        List<String> allergiesList = List.of("allergieTestAdd1", "allergieTestAdd2");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("firstNameTest3");
        medicalRecord.setLastName("lastNameTest3");
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setMedications(medicationList);
        medicalRecord.setAllergies(allergiesList);

        // When
        medicalRecordRepository.save(medicalRecord);

        // Then
        assertEquals(3, medicalRecordListMock.size());
        assertEquals("firstNameTest1", medicalRecordListMock.get(0).getFirstName());
        assertEquals("firstNameTest2", medicalRecordListMock.get(1).getFirstName());
        assertEquals("firstNameTest3", medicalRecordListMock.get(2).getFirstName());
        assertEquals(birthdate, medicalRecordListMock.get(2).getBirthdate());
        assertEquals(medicationList, medicalRecordListMock.get(2).getMedications());
        assertEquals(allergiesList, medicalRecordListMock.get(2).getAllergies());
    }


    @Test
    // On va vérifier ici que la méthode met à jour correctement un MedicalRecord
    void updateMedicalRecord_shouldUpdate() {
        // Given
        LocalDate birthdate = LocalDate.parse("09/01/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList = List.of("medicationTestUpdate1:999mg", "medicationTestUpdate2:999mg");
        List<String> allergiesList = List.of("allergieTestUpdate1", "allergieTestUpdate2");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("firstNameTest1");
        medicalRecord.setLastName("lastNameTest1");
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setMedications(medicationList);
        medicalRecord.setAllergies(allergiesList);

        // When
        medicalRecordRepository.update(medicalRecord);

        // Then
        assertEquals(medicalRecord.getFirstName(), medicalRecordListMock.getFirst().getFirstName());
        assertEquals(medicalRecord.getLastName(), medicalRecordListMock.getFirst().getLastName());
        assertEquals(medicalRecord.getBirthdate(), medicalRecordListMock.getFirst().getBirthdate());
        assertEquals(medicalRecord.getMedications(), medicalRecordListMock.getFirst().getMedications());
        assertEquals(medicalRecord.getAllergies(), medicalRecordListMock.getFirst().getAllergies());

    }

    @Test
    // On va vérifier ici que la méthode supprime correctement un MedicalRecord
    void deleteMedicalRecord_shouldDelete() {
        // Given
        MedicalRecord medicalRecord = medicalRecordListMock.getFirst();

        // When
        medicalRecordRepository.delete(medicalRecord);

        // Then
        assertEquals(1, medicalRecordListMock.size());
        assertFalse(medicalRecordListMock.contains(medicalRecord));
    }

    @Test
    // On va vérifier ici que la méthode vérifie bien l'existence de la String id - True
    void existsById_shouldReturnBooleanWithIdExists(){
        // Given
        String id = medicalRecordListMock.getFirst().getId();

        // When / Then
        assertTrue(medicalRecordRepository.existsById(id));
    }

    @Test
    // On va vérifier ici que la méthode vérifie bien l'existence de la String id - False
    void existsById_shouldReturnBooleanWithIdNotExists(){
        // Given
        String id = "idNotExists";

        // When / Then
        assertFalse(medicalRecordRepository.existsById(id));
    }


}
