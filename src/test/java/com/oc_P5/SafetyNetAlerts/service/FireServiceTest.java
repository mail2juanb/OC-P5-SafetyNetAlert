package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsByAddress;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private FireServiceImpl fireService;

    private List<Person> personListMock;
    private List<Firestation> firestationListMock;
    private List<MedicalRecord> medicalRecordListMock;

    @BeforeEach
    public void SetUp(){
        // Initialisation des mocks fait par l'annotation @ExtendWith(SpringExtension.class)
        // Création des données de test Person
        Person person1 = new Person();
        person1.setFirstName("firstNameTest1");
        person1.setLastName("lastNameTest1");
        person1.setAddress("addressTest1");
        person1.setCity("cityTest1");
        person1.setZip(1);
        person1.setPhone("phoneTest1");
        person1.setEmail("emailTest1");

        Person person2 = new Person();
        person2.setFirstName("firstNameTest2");
        person2.setLastName("lastNameTest2");
        person2.setAddress("addressTest1");
        person2.setCity("cityTest1");
        person2.setZip(1);
        person2.setPhone("phoneTest2");
        person2.setEmail("emailTest2");

        personListMock = new ArrayList<>();
        personListMock.add(person1);
        personListMock.add(person2);

        // Création des données de test Firestation
        Firestation firestation1 = new Firestation();
        firestation1.setAddress("addressTest1");
        firestation1.setStation(1);

        Firestation firestation2 = new Firestation();
        firestation2.setAddress("addressTest2");
        firestation2.setStation(2);

        firestationListMock = new ArrayList<>();
        firestationListMock.add(firestation1);
        firestationListMock.add(firestation2);

        // Création des données de test MedicalRecord
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

        // Configure les mocks
        when(firestationRepository.getAll()).thenReturn(firestationListMock);
        //when(personRepository.getAll()).thenReturn(personListMock);
        when(medicalRecordRepository.getAll()).thenReturn(medicalRecordListMock);
    }

    @Test
    // On va vérifier ici que la méthode retourne correctement les informations des personnes associées à une adresse connue.
    void getFirePersonsByAddress_shouldReturnFirePersonsByAddressWithKnownAddress() {
        // Given
        String address = "addressTest1";
        List<Person> personListMockByAddress = personListMock
                .stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();
        when(personRepository.getByAddress(address)).thenReturn(personListMockByAddress);

        // When
        FirePersonsByAddress result = fireService.getFirePersonsByAddress(address);

        // Then
        assertEquals(address, result.getAddress());
        assertEquals(1, result.getStationNumber());
        assertEquals(2, result.getFirePersonByAdressList().size());

        FirePersonsByAddress.FirePersonByAddress person1 = result.getFirePersonByAdressList().get(0);
        FirePersonsByAddress.FirePersonByAddress person2 = result.getFirePersonByAdressList().get(1);
        assertEquals("lastNameTest1", person1.getLastName());
        assertEquals("lastNameTest2", person2.getLastName());

        assertTrue(result.getFirePersonByAdressList().get(0).getMedications().isEmpty());
        assertEquals(result.getFirePersonByAdressList().get(1).getMedications(), medicalRecordListMock.get(1).getMedications());

        assertTrue(result.getFirePersonByAdressList().get(0).getAllergies().isEmpty());
        assertEquals(result.getFirePersonByAdressList().get(1).getAllergies(), medicalRecordListMock.get(1).getAllergies());
    }

    @Test
    // On va vérifier ici que la méthode retourne correctement les informations des personnes associées à une adresse connue.
    void getFirePersonsByAddress_shouldReturnFirePersonsByAddressWithUnknownAddress() {
        // Given


        // When
        // Then
    }


}
