package com.oc_P5.SafetyNetAlerts.fire;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;

import com.oc_P5.SafetyNetAlerts.service.FireServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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


    @Captor
    ArgumentCaptor<List<String>> personIdsArgumentCaptor;

    private List<Person> personListMock;
    private List<Firestation> firestationListMock;
    private List<MedicalRecord> medicalRecordListMock;
    private List<PersonWithMedicalRecord> personWithMedicalRecordListMock;

    @BeforeEach
    public void SetUp(){
        // Person Test data creation
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1, "phoneTest1", "emailTest1");
        Person person2 = new Person("firstNameTest2", "lastNameTest2", "addressTest1", "cityTest1", 1, "phoneTest2", "emailTest2");

        personListMock = new ArrayList<>();
        personListMock.add(person1);
        personListMock.add(person2);

        // Firestation Test data creation
        Firestation firestation1 = new Firestation("addressTest1", 1);
        Firestation firestation2 = new Firestation("addressTest2", 2);

        firestationListMock = new ArrayList<>();
        firestationListMock.add(firestation1);
        firestationListMock.add(firestation2);

        // MedicalRecord Test data creation
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

        // PersonWithMedicalRecord Test data creation
        PersonWithMedicalRecord person1WithMedicalRecord = new PersonWithMedicalRecord(person1, medicalRecord1);
        PersonWithMedicalRecord person2WithMedicalRecord = new PersonWithMedicalRecord(person2, medicalRecord2);

        personWithMedicalRecordListMock = new ArrayList<>();
        personWithMedicalRecordListMock.add(person1WithMedicalRecord);
        personWithMedicalRecordListMock.add(person2WithMedicalRecord);
    }


    @Test
    void getFirePersonsByAddress_shouldReturnFirePersonsByAddressWithKnownAddress() {
        // Given a known address with associated persons and firestation
        String address = "addressTest1";
        List<String> personIds = new ArrayList<>();
        personIds.add(personListMock.get(0).getId());
        personIds.add(personListMock.get(1).getId());

        when(firestationRepository.existsByAddress(address)).thenReturn(true);
        when(personRepository.findByAddress(address)).thenReturn(Optional.of(personListMock.getFirst()));
        when(firestationRepository.getAll()).thenReturn(firestationListMock);
        when(personRepository.getByAddress(address)).thenReturn(personListMock);
        when(personRepository.getPersonsWithMedicalRecord(personIds)).thenReturn(personWithMedicalRecordListMock);

        // When method is called
        FirePersonsResponse response = fireService.getFirePersonsByAddress(address);

        // Then repository methods are called correctly, and response contains the expected data
        verify(firestationRepository, times(1)).getAll();
        verify(personRepository, times(1)).getByAddress(address);
        verify(personRepository, times(1)).getPersonsWithMedicalRecord(personIds);

        assertEquals(address, response.getAddress());
        assertEquals(1, response.getStationNumber());
        assertEquals(2, response.getFirePersonByAdressList().size());
        assertEquals("lastNameTest1", response.getFirePersonByAdressList().get(0).getLastName());
        assertEquals("lastNameTest2", response.getFirePersonByAdressList().get(1).getLastName());
        assertTrue(response.getFirePersonByAdressList().get(0).getMedications().isEmpty());
        assertEquals(response.getFirePersonByAdressList().get(1).getMedications(), medicalRecordListMock.get(1).getMedications());
        assertTrue(response.getFirePersonByAdressList().get(0).getAllergies().isEmpty());
        assertEquals(response.getFirePersonByAdressList().get(1).getAllergies(), medicalRecordListMock.get(1).getAllergies());

        verify(personRepository).getPersonsWithMedicalRecord(personIdsArgumentCaptor.capture());
        List<String> sentPersonIds = personIdsArgumentCaptor.getValue();
        assertThat(sentPersonIds).isEqualTo(personIds);
    }


    @Test
    void getFirePersonsByAddress_shouldThrowNotFoundExceptionWithUnknownAddressForFirestationList() {
        // Given an unknown address
        String address = "unknownAddressTest1";

        when(firestationRepository.existsByAddress(address)).thenReturn(false);

        // When / Then method throws a NotFoundException with a message containing the address
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> fireService.getFirePersonsByAddress(address));
        assertThat(thrown.getMessage()).contains(address);

        verify(firestationRepository, times(1)).existsByAddress(address);
        verify(firestationRepository, never()).getAll();
        verify(personRepository, never()).getByAddress(address);
        verify(personRepository, never()).getPersonsWithMedicalRecord(anyList());
    }

    @Test
    void getFirePersonsByAddress_shouldThrowNotFoundExceptionWithUnknownAddressForPersonList() {
        // Given an unknown address
        String address = "unknownAddressTest1";

        when(firestationRepository.existsByAddress(address)).thenReturn(true);
        when(personRepository.findByAddress(address)).thenReturn(Optional.empty());

        // When / Then method throws a NotFoundException with a message containing the address
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> fireService.getFirePersonsByAddress(address));
        assertThat(thrown.getMessage()).contains(address);

        verify(personRepository, times(1)).findByAddress(address);
        verify(firestationRepository, never()).getAll();
        verify(personRepository, never()).getByAddress(address);
        verify(personRepository, never()).getPersonsWithMedicalRecord(anyList());
    }

}