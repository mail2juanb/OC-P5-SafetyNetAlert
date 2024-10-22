package com.oc_P5.SafetyNetAlerts.firestation;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import com.oc_P5.SafetyNetAlerts.service.FirestationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @Mock
    private FirestationRepository firestationRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private FirestationServiceImpl firestationService;

    private List<Firestation> firestationListMock;
    private List<Person> personListMock;
    private List<MedicalRecord> medicalRecordMock;


    @BeforeEach
    public void setUp() {
        // NOTE Firestation Test data creation
        Firestation firestation1 = new Firestation("addressTest1", 1);
        Firestation firestation2 = new Firestation("addressTest2", 2);

        firestationListMock = new ArrayList<>();
        firestationListMock.add(firestation1);
        firestationListMock.add(firestation2);

        // NOTE Person Test data creation
        Person person1 = new Person("firstNameTest1", "lastName1", "addressTest1", "cityTest1", 10001, "123-456-7890", "emailTest1@email.fr");
        Person person2 = new Person("firstNameTest2", "lastName2", "addressTest1", "cityTest1", 10001, "123-456-7891", "emailTest2@email.fr");

        personListMock = new ArrayList<>();
        personListMock.add(person1);
        personListMock.add(person2);

        // NOTE MedicalRecord Test data creation
        LocalDate birthdate1 = LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList1 = List.of("allergieTest1", "allergieTest2");

        LocalDate birthdate2 = LocalDate.parse("02/18/2012", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = Collections.emptyList();
        List<String> allergiesList2 = Collections.emptyList();

        MedicalRecord medicalRecord1 = new MedicalRecord("firstNameTest1", "lastNameTest1", birthdate1, medicationList1, allergiesList1);
        MedicalRecord medicalRecord2 = new MedicalRecord("firstNameTest2", "lastNameTest2", birthdate2, medicationList2, allergiesList2);

        medicalRecordMock = new ArrayList<>();
        medicalRecordMock.add(medicalRecord1);
        medicalRecordMock.add(medicalRecord2);
    }



    @Test
    void getPersonsByStation_shouldReturnPersonsByStation() {
        // Given a station_number
        final Integer station_number = 1;

        // Given a list of Firestations
        final List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(firestationListMock.getFirst());

        // Given a list of address
        final Set<String> stationAddresses = new HashSet<>();
        stationAddresses.add("addressTest1");

        // Given a list of Persons
        final List<Person> personsListMock = new ArrayList<>();
        personsListMock.add(personListMock.get(0));
        personsListMock.add(personListMock.get(1));

        when(firestationRepository.existsByStation(station_number)).thenReturn(true);
        when(firestationRepository.getByStation(station_number)).thenReturn(firestationList);
        when(personRepository.getByAddresses(stationAddresses)).thenReturn(personsListMock);
        when(medicalRecordRepository.findById(personsListMock.get(0).getId())).thenReturn(Optional.of(medicalRecordMock.get(0)));
        when(medicalRecordRepository.findById(personsListMock.get(1).getId())).thenReturn(Optional.of(medicalRecordMock.get(1)));

        // When call method on service
        PersonsByStation result = firestationService.getPersonsByStation(station_number);

        // Then verify that the object returned contains expected values
        assertEquals(2, result.getPersons().size());
        assertEquals(1, result.getNbrOfMinors());

        assertEquals(1, result.getNbrOfMinors());
        assertEquals(1, result.nbrOfMajors());
        assertThat(result.getPersons())
                .extracting("firstName", "lastName", "address", "phone")
                        .contains(tuple(personsListMock.getFirst().getFirstName(),
                                personsListMock.getFirst().getLastName(),
                                personsListMock.getFirst().getAddress(),
                                personsListMock.getFirst().getPhone()));
        assertThat(result.getPersons())
                .extracting("firstName", "lastName", "address", "phone")
                .contains(tuple(personsListMock.get(1).getFirstName(),
                        personsListMock.get(1).getLastName(),
                        personsListMock.get(1).getAddress(),
                        personsListMock.get(1).getPhone()));

        verify(firestationRepository, times(1)).existsByStation(station_number);
        verify(firestationRepository, times(1)).getByStation(station_number);
        verify(personRepository, times(1)).getByAddresses(anyCollection());
        verify(medicalRecordRepository, times(2)).findById(anyString());

        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationRepository).getByStation(stationArgumentCaptor.capture());
        Integer capturedStation = stationArgumentCaptor.getValue();
        assertThat(capturedStation)
                .isNotNull()
                .isEqualTo(station_number);
    }


    @Test
    void getPersonsByStation_shouldThrowNotFoundExceptionWhenStationNotExist() {
        // Given an unknown station
        final Integer station_number = 99;

        // When / Then a NotFoundException is thrown
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.getPersonsByStation(station_number));
        assertThat(thrown.getMessage()).contains(station_number.toString());

        verify(firestationRepository, times(1)).existsByStation(station_number);
        verify(firestationRepository, never()).getByStation(station_number);
        verify(personRepository, never()).getByAddresses(anyCollection());
        verify(medicalRecordRepository, never()).existsById(anyString());
    }


    @Test
    void updateFirestation_shouldUpdateFirestationWhenExists() {
        // Given a known Firestation to update
        final Firestation firestation = new Firestation("addressTest1", 10);

        when(firestationRepository.findByAddress(firestation.getAddress())).thenReturn(Optional.of(firestationListMock.getFirst()));

        // When call method on service
        firestationService.updateFirestation(firestation);

        // Then verify that the object sent is correctly distributed
        verify(firestationRepository, times(1)).findByAddress(firestation.getAddress());
        verify(firestationRepository, times(1)).update(any(Firestation.class));

        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationRepository).update(firestationArgumentCaptor.capture());
        Firestation updatedFirestation = firestationArgumentCaptor.getValue();

        assertThat(updatedFirestation)
                .isNotNull()
                .satisfies(f -> {
                    assertThat(f.getAddress()).isEqualTo(firestation.getAddress());
                    assertThat(f.getStation()).isEqualTo(firestation.getStation());
                });
    }

    @Test
    void updateFirestation_shouldThrowNotFoundExceptionWhenNotExists() {
        // Given an unknown Firestation to update
        final Firestation firestation = new Firestation("unknownAddressTest30", 30);

        when(firestationRepository.findByAddress(firestation.getAddress())).thenReturn(Optional.empty());

        // When / Then a NotFoundException is thrown
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.updateFirestation(firestation));
        assertThat(thrown.getMessage()).contains(firestation.getAddress());

        verify(firestationRepository, times(1)).findByAddress(firestation.getAddress());
        verify(firestationRepository, never()).update(firestation);
    }

    @Test
    void addFirestation_shouldAddFirestationWhenNotExists() {
        // Given an unknown Firestation to save
        final Firestation firestation = new Firestation("addressTest13", 1);

        when(firestationRepository.existsByAddress(firestation.getAddress())).thenReturn(false);

        // When call method on service
        firestationService.addFirestation(firestation);

        // Then the new Firestation is saved
        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationRepository).save(firestationArgumentCaptor.capture());

        Firestation savedFirestation = firestationArgumentCaptor.getValue();
        assertThat(savedFirestation)
                .isNotNull()
                .satisfies(f -> {
                    assertThat(f.getAddress()).isEqualTo(firestation.getAddress());
                    assertThat(f.getStation()).isEqualTo(firestation.getStation());
                });

        verify(firestationRepository, times(1)).existsByAddress(anyString());
        verify(firestationRepository, times(1)).save(firestation);
    }

    @Test
    void addFirestation_shouldThrowConflictExceptionWhenExists() {
        // Given a known Firestation to save
        final Firestation firestation = new Firestation("addressTest1", 14);

        when(firestationRepository.existsByAddress(firestation.getAddress())).thenReturn(true);

        // When / Then  a ConflictException is thrown
        ConflictException thrown = assertThrows(ConflictException.class, () -> firestationService.addFirestation(firestation));
        assertThat(thrown.getMessage()).contains(firestation.getAddress());

        verify(firestationRepository, times(1)).existsByAddress(anyString());
        verify(firestationRepository, never()).save(firestation);
    }

    @Test
    void deleteFirestationByAddress_shouldRemoveFirestationWhenAddressExists() {
        // Given a known address to delete Firestation
        final String address = "addressTest1";

        when(firestationRepository.existsByAddress(address)).thenReturn(true);

        // When call method on service
        firestationService.deleteFirestationByAddress(address);

        // Then Firestation with address given is deleted
        ArgumentCaptor<String> addressArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(firestationRepository).deleteByAddress(addressArgumentCaptor.capture());

        String deletedAddress = addressArgumentCaptor.getValue();
        assertThat(deletedAddress).isEqualTo(address);

        verify(firestationRepository, times(1)).existsByAddress(address);
        verify(firestationRepository, times(1)).deleteByAddress(address);
    }

    @Test
    void deleteFirestationByAddress_shouldThrowNotFoundExceptionWhenFirestationNotExist() {
        // Given an unknown address
        final String address = "unknownAddressTest30";

        // When / Then a NotFoundException is thrown
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestationByAddress(address));
        assertThat(thrown.getMessage()).contains(address);

        verify(firestationRepository, times(1)).existsByAddress(address);
        verify(firestationRepository, never()).deleteByAddress(address);
    }

    @Test
    void deleteFirestationByStation_shouldRemoveFirestationWhenStationExists() {
        // Given a known station to delete Firestation
        final Integer station_number = 1;

        when(firestationRepository.existsByStation(station_number)).thenReturn(true);

        // When call method on service
        firestationService.deleteFirestationByStation(station_number);

        // Then Firestation with station given are deleted
        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationRepository).deleteByStation(stationArgumentCaptor.capture());

        Integer deletedStation = stationArgumentCaptor.getValue();
        assertThat(deletedStation).isEqualTo(station_number);

        verify(firestationRepository, times(1)).existsByStation(station_number);
        verify(firestationRepository, times(1)).deleteByStation(station_number);
    }

    @Test
    void deleteFirestationByStation_shouldThrowNotFoundExceptionWhenFirestationNotExist() {
        // Given an unknown station
        final Integer station_number = 89;

        // When / Then a NotFoundException is thrown
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestationByStation(station_number));
        assertThat(thrown.getMessage()).contains(station_number.toString());

        verify(firestationRepository, times(1)).existsByStation(station_number);
        verify(firestationRepository, never()).deleteByStation(station_number);
    }

}