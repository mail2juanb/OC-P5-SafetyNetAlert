package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        // NOTE Initialisation des mocks fait par l'annotation @ExtendWith(MockitoExtension.class)

        // Création des données de test pour firestation
        Firestation firestation1 = new Firestation();
        firestation1.setAddress("addressTest1");
        firestation1.setStation(1);

        Firestation firestation2 = new Firestation();
        firestation2.setAddress("addressTest2");
        firestation2.setStation(2);

        firestationListMock = new ArrayList<>();
        firestationListMock.add(firestation1);
        firestationListMock.add(firestation2);

        // Création des données de test pour Person
        Person person1 = new Person();
        person1.setFirstName("firstNameTest1");
        person1.setLastName("lastName1");
        person1.setAddress("addressTest1");
        person1.setCity("cityTest1");
        person1.setZip(10001);
        person1.setPhone("123-456-7890");
        person1.setEmail("emailTest1@email.fr");

        Person person2 = new Person();
        person2.setFirstName("firstNameTest2");
        person2.setLastName("lastName2");
        person2.setAddress("addressTest1");
        person2.setCity("cityTest1");
        person2.setZip(10001);
        person2.setPhone("123-456-7891");
        person2.setEmail("emailTest2@email.fr");

        personListMock = new ArrayList<>();
        personListMock.add(person1);
        personListMock.add(person2);

        // Création des données de test pour MedicalRecord
        LocalDate birthdate1 = LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList1 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("firstNameTest1");
        medicalRecord1.setLastName("lastNameTest1");
        medicalRecord1.setBirthdate(birthdate1);
        medicalRecord1.setMedications(medicationList1);
        medicalRecord1.setAllergies(allergiesList1);

        LocalDate birthdate2 = LocalDate.parse("02/18/2012", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = Collections.emptyList();
        List<String> allergiesList2 = Collections.emptyList();

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("firstNameTest2");
        medicalRecord2.setLastName("lastNameTest2");
        medicalRecord2.setBirthdate(birthdate2);
        medicalRecord2.setMedications(medicationList2);
        medicalRecord2.setAllergies(allergiesList2);

        medicalRecordMock = new ArrayList<>();
        medicalRecordMock.add(medicalRecord1);
        medicalRecordMock.add(medicalRecord2);

        // Configure les mocks
        //when(firestationRepository.getAll()).thenReturn(firestationListMock);
    }

    @Test
    // On va vérifier ici que la méthode retourne bien la liste des firestations
    void getFirestationsService_shouldReturnListOfFirestations() {
        // Given
        when(firestationRepository.getAll()).thenReturn(firestationListMock);

        // When
        List<Firestation> result = firestationService.getFirestations();

        // Then
        assertEquals(firestationListMock, result);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la mise à jour du mapping de firestation existant
    void updateFirestationMappingService_shouldUpdateFirestationWhenExists() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("addressTest1");
        firestation.setStation(10);

        // When
        when(firestationRepository.findByAddress(firestation.getAddress())).thenReturn(Optional.of(firestationListMock.getFirst()));
        firestationService.updateFirestation(firestation);

        // Then
        assertEquals(10, firestationListMock.getFirst().getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la levée d'exception lorsque l'on met à jour le mapping d'une firestation inexistant
    void updateFirestation_shouldThrowNotFoundExceptionWhenNotExists() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("unknownAddressTest30");
        firestation.setStation(30);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.updateFirestation(firestation));
        assertThat(thrown.getMessage()).contains(firestation.getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la levée d'exception lorsque la Firestation est vide ou null
    void updateFirestation_shouldThrowNullOrEmptyObjectException() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress(" ");
        firestation.setStation(null);

        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> firestationService.updateFirestation(firestation));
        assertThat(thrown.getMessage()).satisfiesAnyOf(
                message -> assertThat(message).contains("null"),
                message -> assertThat(message).contains("empty")
        );
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la levée d'exception lorsque la Firestation est vide ou null
    void addFirestation_shouldThrowNullOrEmptyObjectException() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress(" ");
        firestation.setStation(null);

        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> firestationService.addFirestation(firestation));
        assertThat(thrown.getMessage()).satisfiesAnyOf(
                message -> assertThat(message).contains("null"),
                message -> assertThat(message).contains("empty")
        );
    }

    @Test
    // On va vérifier ici que lorsqu'une nouvelle caserne est ajoutée, elle est correctement sauvegardée avec les bons attributs.
    void addFirestationMappingService_shouldAddNewFirestationWhenNotExists() {
        // Given a firestation
        Firestation firestation = new Firestation();
        firestation.setAddress("addressTest13");
        firestation.setStation(1);

        when(firestationRepository.existsByAddress(firestation.getAddress())).thenReturn(false);

        // When
        firestationService.addFirestation(firestation);

        // Then the firestation is corretly saved
        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationRepository).save(firestationArgumentCaptor.capture());

        Firestation savedFirestation = firestationArgumentCaptor.getValue();

        assertThat(savedFirestation)
                .isNotNull()
                .satisfies(f -> {
                    assertThat(f.getAddress()).isEqualTo(firestation.getAddress());
                    assertThat(f.getStation()).isEqualTo(firestation.getStation());
                });
    }

    @Test
    // On va vérifier ici le bon fonctionnement de l'ajout d'une firestation (existant)
    void addFirestation_shouldThrowConflictExceptionWhenExists() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("addressTest1");
        firestation.setStation(14);

        when(firestationRepository.existsByAddress(firestation.getAddress())).thenReturn(true);

        // When / Then
        ConflictException thrown = assertThrows(ConflictException.class, () -> firestationService.addFirestation(firestation));
        assertThat(thrown.getMessage()).contains(firestation.getAddress());
    }

    @Test
    // On va vérifier ici que la suppression d'une Firestation grace une Firestation est correctement supprimée
    void deleteFirestation_shouldRemoveFirestationWhenAddressAndStationNumberExist() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("addressTest1");
        firestation.setStation(1);

        when(firestationRepository.existsByAddressByStation(any(Firestation.class))).thenReturn(true);

        // When
        firestationService.deleteFirestation(firestation);

        // Then
        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationRepository).delete(firestationArgumentCaptor.capture());

        Firestation deletedFirestation = firestationArgumentCaptor.getValue();

        assertThat(deletedFirestation)
                .isNotNull()
                .satisfies(f -> {
                    assertThat(f.getAddress()).isEqualTo(firestation.getAddress());
                    assertThat(f.getStation()).isEqualTo(firestation.getStation());
                });
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la levée d'exception lorsque l'address de l'objet Firestation n'existe pas
    void deleteFirestation_shouldThrowNotFoundExceptionWhenFirestationNotExist() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("unknownAddressTest30");
        firestation.setStation(null);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestation(firestation));
        assertThat(thrown.getMessage()).contains(firestation.getAddress());
    }

    @Test
    // On va vérifier ici la suppression d'une Firestation grace une addresse lorsque l'address existe
    void deleteFirestation_shouldRemoveFirestationByAddressWhenExists() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("addressTest1");
        firestation.setStation(null);

        when(firestationRepository.existsByAddress(anyString())).thenReturn(true);

        // When
        firestationService.deleteFirestation(firestation);

        // Then
        ArgumentCaptor<String> addressCaptor = ArgumentCaptor.forClass(String.class);
        verify(firestationRepository).deleteByAddress(addressCaptor.capture());

        String deletedAddress = addressCaptor.getValue();

        assertNotNull(deletedAddress);
        assertEquals(firestation.getAddress(), deletedAddress);
    }

    @Test
    // On va vérifier ici la levée d'exception lors de la suppression d'une Firestation grace à une address qui n'existe pas
    void deleteFirestation_shouldThrowNotFoundExceptionWhenAddressNotExist() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("unknownAddressTest30");
        firestation.setStation(30);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestation(firestation));
        assertThat(thrown.getMessage()).contains(firestation.getAddress());
    }

    @Test
    // On va vérifier ici la suppression d'une Firestation grace un stationNumber lorsqu'il existe
    void deleteFirestation_shouldRemoveFirestationByStationWhenExists() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress(null);
        firestation.setStation(1);

        when(firestationRepository.existsByStation(firestation.getStation())).thenReturn(true);

        // When
        firestationService.deleteFirestation(firestation);

        // Then
        ArgumentCaptor<Integer> stationCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationRepository).deleteByStation(stationCaptor.capture());

        Integer deletedStation = stationCaptor.getValue();

        assertNotNull(deletedStation);
        assertEquals(firestation.getStation(), deletedStation);
    }

    @Test
    // On va vérifier ici la levée d'exception lors de la suppression d'une Firestation grace à une station qui n'existe pas
    void deleteFirestation_shouldThrowNotFoundExceptionWhenStationNotExist() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress(null);
        firestation.setStation(99);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestation(firestation));
        assertThat(thrown.getMessage()).contains(firestation.getStation().toString());
    }

    @Test
    // On va vérifier ici la levée d'exception lors de la suppression d'une Firestation grace à une address et une station null
    void deleteFirestation_shouldThrowConflictExceptionWhenAddressAndStationNull() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress(null);
        firestation.setStation(null);

        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> firestationService.deleteFirestation(firestation));
        assertThat(thrown.getMessage()).satisfiesAnyOf(
                message -> assertThat(message).contains("null"),
                message -> assertThat(message).contains("empty")
        );
    }

    @Test
    // On va vérifier ici la levée d'exception lors de la récupération de la liste des PersonsByStation, lorsque la station n'existe pas
    void getPersonsByStation_shouldThrowNotFoundExceptionWhenStationNotExist() {
        // Given
        Integer stationNumber = 99;

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.getPersonsByStation(stationNumber));
        assertThat(thrown.getMessage()).contains(stationNumber.toString());
    }

    @Test
    // On va vérifier ici la liste des PersonsByStation avec une station valide
    void getPersonsByStation_shouldReturnListOfPersonsByStation() {
        // Given
        Integer stationNumber = 1;

        List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(firestationListMock.getFirst());

        Set<String> stationAddresses = new HashSet<>();
        stationAddresses.add("addressTest1");

        List<Person> personsListMock = new ArrayList<>();
        personsListMock.add(personListMock.get(0));
        personsListMock.add(personListMock.get(1));

        when(firestationRepository.existsByStation(stationNumber)).thenReturn(true);
        when(firestationRepository.getByStation(stationNumber)).thenReturn(firestationList);
        when(personRepository.getByAddresses(stationAddresses)).thenReturn(personsListMock);
        when(medicalRecordRepository.findById(personsListMock.get(0).getId())).thenReturn(Optional.of(medicalRecordMock.get(0)));
        when(medicalRecordRepository.findById(personsListMock.get(1).getId())).thenReturn(Optional.of(medicalRecordMock.get(1)));

        // When
        PersonsByStation result = firestationService.getPersonsByStation(stationNumber);

        // Then
        assertEquals(2, result.getPersons().size());
        assertEquals(1, result.getNbrOfMinors());
    }

}
