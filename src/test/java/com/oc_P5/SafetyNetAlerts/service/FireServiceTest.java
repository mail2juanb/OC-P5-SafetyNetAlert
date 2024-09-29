package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsByAddress;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
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
        lenient().when(firestationRepository.getAll()).thenReturn(firestationListMock);
        lenient().when(personRepository.getAll()).thenReturn(personListMock);
        lenient().when(medicalRecordRepository.getAll()).thenReturn(medicalRecordListMock);
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

    @ParameterizedTest
    @MethodSource("provideInvalidAddresses")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException avec une addresse Blank
    void getFirePersonsByAddress_shouldReturnFNullOrEmptyObjectExceptionWithBlankAddress(String address){
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> fireService.getFirePersonsByAddress(address));
        assertThat(thrown.getMessage().contains("null"));
    }

    @Test
    // On va vérifier ici que la méthode retourne une exception si l'adresse n'est pas reconnue.
    void getFirePersonsByAddress_shouldThrowExceptionForUnknownAddress() {
        // Given
        String address = "unknownAddressTest1";

        // Configure mock pour retourner une liste vide lorsque l'adresse est inconnue
        //when(personRepository.getByAddress(address)).thenReturn(Collections.emptyList());

        // When / Then
        assertThrows(NoSuchElementException.class, () -> fireService.getFirePersonsByAddress(address));
    }

    @Test
    // On va vérifier ici que la méthode retourne une liste vide si l'adresse est valide mais qu'il n'y a pas de personnes associées.
    void getFirePersonsByAddress_shouldReturnEmptyListWhenNoPersonsFound() {
        // Given
        String address = "addressTest1";

        // Configure mock pour retourner une liste vide de personnes
        when(personRepository.getByAddress(address)).thenReturn(Collections.emptyList());

        // When
        FirePersonsByAddress result = fireService.getFirePersonsByAddress(address);

        // Then
        assertEquals(address, result.getAddress());
        assertEquals(1, result.getStationNumber());
        assertTrue(result.getFirePersonByAdressList().isEmpty());
    }

    @Test
    // Vérifie que les getters de PersonWithMedicalRecord retournent les bonnes valeurs
    void personWithMedicalRecord_gettersShouldReturnCorrectValues() {
        // Given
        Person person = new Person();
        person.setFirstName("firstNameTest");
        person.setLastName("lastNameTest");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("firstNameTest");
        medicalRecord.setLastName("lastNameTest");

        // When
        FireServiceImpl.PersonWithMedicalRecord personWithMedicalRecord = new FireServiceImpl.PersonWithMedicalRecord(person, medicalRecord);

        // Then
        assertEquals("firstNameTest", personWithMedicalRecord.getFirstName());
        assertEquals("lastNameTest", personWithMedicalRecord.getLastName());
    }



    // Fournit des valeurs d'adresse, y compris null
    static Stream<String> provideInvalidAddresses() {
        return Stream.of("  ", "", "   ", null);
    }


}
