package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FireServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private FireServiceImpl fireService;

    // Pour typer correctement la liste
    @Captor
    ArgumentCaptor<List<String>> personIdsArgumentCaptor;

    private List<Person> personListMock;
    private List<Firestation> firestationListMock;
    private List<MedicalRecord> medicalRecordListMock;
    private List<PersonWithMedicalRecord> personWithMedicalRecordListMock;

    @BeforeEach
    public void SetUp(){
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

        PersonWithMedicalRecord person1WithMedicalRecord = new PersonWithMedicalRecord(person1, medicalRecord1);
        PersonWithMedicalRecord person2WithMedicalRecord = new PersonWithMedicalRecord(person2, medicalRecord2);

        personWithMedicalRecordListMock = new ArrayList<>();
        personWithMedicalRecordListMock.add(person1WithMedicalRecord);
        personWithMedicalRecordListMock.add(person2WithMedicalRecord);
    }



    @Test
    // On va vérifier ici que la méthode retourne correctement les informations des personnes associées à une adresse connue.
    void getFirePersonsByAddress_shouldReturnFirePersonsByAddressWithKnownAddress() {
        // Given
        String address = "addressTest1";
        List<String> personIds = new ArrayList<>();
        personIds.add(personListMock.get(0).getId());
        personIds.add(personListMock.get(1).getId());

        when(firestationRepository.existsByAddress(address)).thenReturn(true);
        when(personRepository.findByAddress(address)).thenReturn(Optional.of(personListMock.getFirst()));
        when(firestationRepository.getAll()).thenReturn(firestationListMock);
        when(personRepository.getByAddress(address)).thenReturn(personListMock);
        when(personRepository.getPersonsWithMedicalRecord(personIds)).thenReturn(personWithMedicalRecordListMock);

        // When
        FirePersonsResponse result = fireService.getFirePersonsByAddress(address);

        // Then
        verify(firestationRepository, times(1)).getAll();
        verify(personRepository, times(1)).getByAddress(address);
        verify(personRepository, times(1)).getPersonsWithMedicalRecord(personIds);

        assertEquals(address, result.getAddress());
        assertEquals(1, result.getStationNumber());
        assertEquals(2, result.getFirePersonByAdressList().size());
        assertEquals("lastNameTest1", result.getFirePersonByAdressList().get(0).getLastName());
        assertEquals("lastNameTest2", result.getFirePersonByAdressList().get(1).getLastName());
        assertTrue(result.getFirePersonByAdressList().get(0).getMedications().isEmpty());
        assertEquals(result.getFirePersonByAdressList().get(1).getMedications(), medicalRecordListMock.get(1).getMedications());
        assertTrue(result.getFirePersonByAdressList().get(0).getAllergies().isEmpty());
        assertEquals(result.getFirePersonByAdressList().get(1).getAllergies(), medicalRecordListMock.get(1).getAllergies());

        verify(personRepository).getPersonsWithMedicalRecord(personIdsArgumentCaptor.capture());
        List<String> sentPersonIds = personIdsArgumentCaptor.getValue();
        assertThat(sentPersonIds).isEqualTo(personIds);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAddresses")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException avec une addresse Blank
    void getFirePersonsByAddress_shouldReturnFNullOrEmptyObjectExceptionWithBlankAddress(String address){
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> fireService.getFirePersonsByAddress(address));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
    }

    @Test
    // On va vérifier ici que la méthode retourne une exception si l'adresse n'est pas reconnue dans la liste des Firestation.
    void getFirePersonsByAddress_shouldThrowNotFoundExceptionWithUnknownAddressForFirestationList() {
        // Given
        String address = "unknownAddressTest1";

        when(firestationRepository.existsByAddress(address)).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> fireService.getFirePersonsByAddress(address));
        assertThat(thrown.getMessage()).contains(address);

        verify(firestationRepository, times(1)).existsByAddress(address);
    }

    @Test
    // On va vérifier ici que la méthode retourne une exception si l'adresse n'est pas reconnue dans la liste des Firestation.
    void getFirePersonsByAddress_shouldThrowNotFoundExceptionWithUnknownAddressForPersonList() {
        // Given
        String address = "unknownAddressTest1";

        when(firestationRepository.existsByAddress(address)).thenReturn(true);
        when(personRepository.findByAddress(address)).thenReturn(Optional.empty());

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> fireService.getFirePersonsByAddress(address));
        assertThat(thrown.getMessage()).contains(address);

        verify(personRepository, times(1)).findByAddress(address);
    }



    // Fournit des valeurs d'adresse, y compris null
    static Stream<String> provideInvalidAddresses() {
        return Stream.of("  ", "", "   ", null);
    }


}
