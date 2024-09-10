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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class MedicalRecordRepositoryTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private MedicalRecordRepositoryImpl medicalRecordRepository;

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

        medicalRecordListMock = new ArrayList<>();
        medicalRecordListMock.add(medicalRecordTest1);
        medicalRecordListMock.add(medicalRecordTest2);

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setMedicalRecords(medicalRecordListMock);

        //Configure le comportement du mock
        //when(dataReaderService.getData()).thenReturn(dataWrapperList);
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
    void addMedicalRecordMapping_shouldAddNewMedicalRecord() {
        // Given
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

        medicalRecordRepository.save(medicalRecordTestAdd);

        // Then
        assertEquals(3, medicalRecordListMock.size());
        assertEquals("firstNameTest1", medicalRecordListMock.get(0).getFirstName());
        assertEquals("firstNameTest2", medicalRecordListMock.get(1).getFirstName());
        assertEquals("firstNameTest3", medicalRecordListMock.get(2).getFirstName());
        assertEquals(birthdateTestAdd, medicalRecordListMock.get(2).getBirthdate());
        assertEquals(medicationListTestAdd, medicalRecordListMock.get(2).getMedications());
        assertEquals(allergiesListTestAdd, medicalRecordListMock.get(2).getAllergies());
    }

    //public Optional<MedicalRecord> updateMedicalRecordMapping(MedicalRecord updateMedicalRecord) {
    // FIXME Problème de Mock ? Comment faire ?
    @Test
    // On va vérifier ici que la méthode met à jour correctement un MedicalRecord
    void updateMedicalRecordMapping_shouldReturnOptionalMedicalRecordUpdated() {
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


//        when(medicalRecordRepository.findMedicalRecordById(medicalRecordTestUpdate.getId())).thenReturn(Optional.of(medicalRecordTestUpdate));
//        when(medicalRecordRepository.updateMedicalRecordMapping(medicalRecordTestUpdate)).thenReturn(Optional.of(medicalRecordTestUpdate));

        // When
        medicalRecordRepository.update(medicalRecord);


        // Then
        assertEquals(medicalRecord.getFirstName(), medicalRecordListMock.getFirst().getFirstName());
        assertEquals(medicalRecord.getLastName(), medicalRecordListMock.getFirst().getLastName());
        assertEquals(medicalRecord.getBirthdate(), medicalRecordListMock.getFirst().getBirthdate());
        assertEquals(medicalRecord.getMedications(), medicalRecordListMock.getFirst().getMedications());
        assertEquals(medicalRecord.getAllergies(), medicalRecordListMock.getFirst().getAllergies());

    }

}
