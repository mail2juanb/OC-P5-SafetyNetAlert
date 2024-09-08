package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.service.MedicalRecordServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {

    @Mock
    private DataReader dataReaderService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private List<MedicalRecord> medicalRecordListMock;


    @BeforeEach
    public void setUp() {
        // Initialisation des mocks fait par l'annotation @ExtendWith(MockitoExtension.class)
        // Creation des données de test - String *firstName*, String *lastName*, LocalDate birthdate, List<String> medications, List<String> allergies (* : required)
        LocalDate birthdateTest1 = LocalDate.parse("09/01/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationListTest1 = Collections.emptyList();
        List<String> allergiesListTest1 = Collections.emptyList();

        MedicalRecord medicalRecordTest1 = new MedicalRecord();
        medicalRecordTest1.setFirstName("firstNameTest1");
        medicalRecordTest1.setLastName("lastNameTest1");
        medicalRecordTest1.setBirthdate(birthdateTest1);
        medicalRecordTest1.setMedications(medicationListTest1);
        medicalRecordTest1.setAllergies(allergiesListTest1);

        LocalDate birthdateTest2 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationListTest2 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesListTest2 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecordTest2 = new MedicalRecord();
        medicalRecordTest2.setFirstName("firstNameTest2");
        medicalRecordTest2.setLastName("lastNameTest2");
        medicalRecordTest2.setBirthdate(birthdateTest2);
        medicalRecordTest2.setMedications(medicationListTest2);
        medicalRecordTest2.setAllergies(allergiesListTest2);

        medicalRecordListMock = new ArrayList<>(Arrays.asList(medicalRecordTest1, medicalRecordTest2));

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setMedicalRecords(medicalRecordListMock);

        //Configure le comportement du mock
        //when(dataReaderService.getData()).thenReturn(dataWrapperList);
        // Configure le mock pour MedicalRecordRepository
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(medicalRecordListMock);
    }

    @Test
    // On va vérifier ici que la méthode retourne bien les données mock
    void getMedicalRecord_shouldReturnListOfMedicalRecords() {
        // When
        List<MedicalRecord> medicalRecordList = medicalRecordService.getMedicalRecordsService();

        // Then
        assertEquals(2, medicalRecordList.size());
        assertEquals("firstNameTest1", medicalRecordList.get(0).getFirstName());
        assertEquals("firstNameTest2", medicalRecordList.get(1).getFirstName());
    }

    @Test
    // On va vérifier ici que la méthode ajoute correctement un MedicalRecord
    void addMedicalRecordMapping_shouldAddNewMedicalRecord() {
        // Given
        List<MedicalRecord> medicalRecordList = medicalRecordService.getMedicalRecordsService();
        LocalDate birthdateTestAdd = LocalDate.parse("09/01/1999", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationListTestAdd = List.of("medicationTestAdd1:999mg", "medicationTestAdd2:999mg");
        List<String> allergiesListTestAdd = List.of("allergieTestAdd1", "allergieTestAdd2");

        MedicalRecord medicalRecordTestAdd = new MedicalRecord();
        medicalRecordTestAdd.setFirstName("firstNameTest3");
        medicalRecordTestAdd.setLastName("lastNameTest3");
        medicalRecordTestAdd.setBirthdate(birthdateTestAdd);
        medicalRecordTestAdd.setMedications(medicationListTestAdd);
        medicalRecordTestAdd.setAllergies(allergiesListTestAdd);

        // When
        medicalRecordList.add(medicalRecordTestAdd);

        // Then
        assertEquals(3, medicalRecordList.size());
        assertEquals("firstNameTest1", medicalRecordList.get(0).getFirstName());
        assertEquals("firstNameTest2", medicalRecordList.get(1).getFirstName());
        assertEquals("firstNameTest3", medicalRecordList.get(2).getFirstName());
        assertEquals(birthdateTestAdd, medicalRecordList.get(2).getBirthdate());
        assertEquals(medicationListTestAdd, medicalRecordList.get(2).getMedications());
        assertEquals(allergiesListTestAdd, medicalRecordList.get(2).getAllergies());
    }

    //public Optional<MedicalRecord> updateMedicalRecordMapping(MedicalRecord updateMedicalRecord) {
    // FIXME Problème de Mock ? Comment faire ?
    @Test
    // On va vérifier ici que la méthode met à jour correctement un MedicalRecord
    void updateMedicalRecordMapping_shouldReturnOptionalMedicalRecordUpdated() {
        // Given
        LocalDate birthdateTestUpdate = LocalDate.parse("09/01/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationListTestUpdate = List.of("medicationTestUpdate1:999mg", "medicationTestUpdate2:999mg");
        List<String> allergiesListTestUpdate = List.of("allergieTestUpdate1", "allergieTestUpdate2");

        MedicalRecord medicalRecordTestUpdate = new MedicalRecord();
        medicalRecordTestUpdate.setFirstName("firstNameTest1");
        medicalRecordTestUpdate.setLastName("lastNameTest1");
        medicalRecordTestUpdate.setBirthdate(birthdateTestUpdate);
        medicalRecordTestUpdate.setMedications(medicationListTestUpdate);
        medicalRecordTestUpdate.setAllergies(allergiesListTestUpdate);

        //when(medicalRecordRepository.findMedicalRecordById(medicalRecordTestUpdate.getId())).thenReturn(Optional.of(medicalRecordTestUpdate));
        //when(medicalRecordRepository.updateMedicalRecordMapping(medicalRecordTestUpdate)).thenReturn(Optional.of(medicalRecordTestUpdate));

        // When
        Optional<MedicalRecord> updateMedicalRecordOpt = medicalRecordRepository.updateMedicalRecordMapping(medicalRecordTestUpdate);
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.getMedicalRecords();


        // Then
        assertTrue(updateMedicalRecordOpt.isPresent());
        assertEquals(medicalRecordTestUpdate.getFirstName(), medicalRecordList.getFirst().getFirstName());
        assertEquals(medicalRecordTestUpdate.getLastName(), medicalRecordList.getFirst().getLastName());
        assertEquals(medicalRecordTestUpdate.getBirthdate(), medicalRecordList.getFirst().getBirthdate());
        assertEquals(medicalRecordTestUpdate.getMedications(), medicalRecordList.getFirst().getMedications());
        assertEquals(medicalRecordTestUpdate.getAllergies(), medicalRecordList.getFirst().getAllergies());

    }

}
