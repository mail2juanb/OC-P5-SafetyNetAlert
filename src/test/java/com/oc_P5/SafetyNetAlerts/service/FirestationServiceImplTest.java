package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceImplTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private FirestationServiceImpl firestationService;

    private Firestation firestation;
    private List<Firestation> firestationList;
    private List<Person> personList;
    private MedicalRecord minorMedicalRecord;
    private MedicalRecord adultMedicalRecord;


    @BeforeEach
    public void setUp() {
        // NOTE Initialisation des mocks fait par l'annotation @ExtendWith(MockitoExtension.class)

        // Création des données de test pour firestation
        firestation = new Firestation(" == DataAdressTest 0 == ", 0);
        firestationList = Arrays.asList(
                new Firestation(" == DataAddressTest 1 == ", 1),
                new Firestation(" == DataAddressTest 1bis == ", 1),
                new Firestation(" == DataAddressTest 2 == ", 2)
        );

        // Création des données de test pour person
        personList = Arrays.asList(
                new Person("Bernard", "Martin", " == DataAddressTest 1 == ", "Cityname1", 12345, "123-456-7890", "bernard.martin@example.com"),
                new Person("BernardBis", "MartinBis", " == DataAddressTest 1bis == ", "Cityname1Bis", 12345, "123-456-7890", "bernardBis.martinBis@example.com"),
                new Person("Nathalie", "Marchand", " == DataAddressTest 2 == ", "Cityname2", 12345, "123-456-7890", "nathalie.marchand@example.com")
        );

        // Création des données de test pour MedicalRecord
        minorMedicalRecord = new MedicalRecord("Bernard", "Martin", LocalDate.of(2021, 1, 1), Arrays.asList(), Arrays.asList());
        adultMedicalRecord = new MedicalRecord("BernardBis", "MartinBis", LocalDate.of(1983, 2, 3), Arrays.asList(), Arrays.asList());
        adultMedicalRecord = new MedicalRecord("Nathalie", "Marchand", LocalDate.of(1980, 2, 2), Arrays.asList("Aspirin"), Arrays.asList("Penicillin"));
    }


    @Test
    // On va vérifier ici que la méthode retourne bien la liste des firestations
    // et qu'elle ne déclenche la méthode du repository associée qu'une seule fois
    void getFirestationsService_shouldReturnListOfFirestations() {
        // Given
        when(firestationRepository.getFirestations()).thenReturn(firestationList);

        // When
        List<Firestation> result = firestationService.getFirestationsService();

        // Then
        assertEquals(firestationList, result);
        verify(firestationRepository, times(1)).getFirestations();
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la mise à jour du mapping de firestation (existant)
    void updateFirestationMappingService_shouldUpdateFirestationWhenExists() {
        // Given
        Firestation updateFirestation = new Firestation(" == DataAddressTest 100 == ", 100);
        when(firestationRepository.firestationByAddressExists(updateFirestation.getAddress())).thenReturn(true);

        // When
        firestationService.updateFirestationMappingService(updateFirestation);

        // Then
        verify(firestationRepository, times(1)).updateFirestationMapping(updateFirestation);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la mise à jour du mapping de firestation (inexistant)
    void updateFirestationMappingService_shouldThrowNotFoundExceptionWhenNotExists() {
        // Given
        Firestation updateFirestation = new Firestation(" == DataAddressTest 100 == ", 100);
        when(firestationRepository.firestationByAddressExists(updateFirestation.getAddress())).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () ->firestationService.updateFirestationMappingService(updateFirestation));
        assertEquals("Firestation doesn't exist", thrown.getMessage());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de l'ajout du mapping de firestation (inexistant)
    void addFirestationMappingService_shouldAddNewFirestationWhenNotExists() {
        // Given
        Firestation addFirestation = new Firestation(" == DataAddressTest 3 == ", 3);
        when(firestationRepository.firestationByAddressExists(addFirestation.getAddress())).thenReturn(false);

        // When
        firestationService.addFirestationMappingService(addFirestation);

        // Then
        verify(firestationRepository, times(1)).addFirestationMapping(addFirestation);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de l'ajout du mapping de firestation (existant)
    void addFirestationMappingService_shouldThrowConflictExceptionWhenExists() {
        // Given
        Firestation addFirestation = new Firestation(" == DataAddressTest 1 == ", 1);
        when(firestationRepository.firestationByAddressExists(addFirestation.getAddress())).thenReturn(true);

        // When / Then
        ConflictException thrown = assertThrows(ConflictException.class, () -> firestationService.addFirestationMappingService(addFirestation));
        assertEquals("Firestation already exists", thrown.getMessage());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression du mapping (address, station) de firestation (existant)
    void deleteFirestationMappingService_shouldRemoveFirestationWhenAddressAndStationNumberExist() {
        // Given
        when(firestationRepository.firestationByAddressByStationExists(any(Firestation.class))).thenReturn(true);

        // When
        firestationService.deleteFirestationMappingService(" == DataAddressTest 1 == ", 1);

        // Then
        verify(firestationRepository, times(1)).deleteFirestationMapping(any(Firestation.class));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la non suppression du mapping (address, station) de firestation (inexistant)
    void deleteFirestationMappingService_shouldThrowNotFoundExceptionWhenAddressAndStationNumberNotExist() {
        // Given
        when(firestationRepository.firestationByAddressByStationExists(any(Firestation.class))).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestationMappingService(" == DataAddressTest 3 == ", 3));
        assertEquals("Firestation doesn't exist", thrown.getMessage());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression du mapping de firestation par adresse (existant)
    void deleteFirestationMappingService_shouldRemoveFirestationByAddressWhenExists() {
        // Given
        String deleteAddress = " == DataAddressTest 1 == ";
        when(firestationRepository.firestationByAddressExists(anyString())).thenReturn(true);

        // When
        firestationService.deleteFirestationMappingService(deleteAddress, null);

        // Then
        verify(firestationRepository, times(1)).deleteFirestationMappingByAddress(deleteAddress);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la non suppression du mapping de firestation par adresse (inexistant)
    void deleteFirestationMappingService_shouldThrowNotFoundExceptionWhenAddressNotExist() {
        // Given
        String deleteAddress = " == DataAddressTest Unknown == ";
        when(firestationRepository.firestationByAddressExists(anyString())).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestationMappingService(deleteAddress, null));
        assertEquals("Address doesn't exist", thrown.getMessage());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression du mapping de firestation par station (existant)
    void deleteFirestationMappingService_shouldRemoveFirestationByStationWhenExists() {
        // Given
        Integer stationNumber = 1;
        when(firestationRepository.firestationByStationExists(stationNumber)).thenReturn(true);

        // When
        firestationService.deleteFirestationMappingService(null, stationNumber);

        // Then
        verify(firestationRepository, times(1)).deleteFirestationMappingByStation(stationNumber);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la non suppression du mapping de firestation par station (inexistant)
    void deleteFirestationMappingService_shouldThrowNotFoundExceptionWhenStationNotExist() {
        // Given
        Integer stationNumber = 999;
        when(firestationRepository.firestationByStationExists(stationNumber)).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> firestationService.deleteFirestationMappingService(null, stationNumber));
        assertEquals("Station number doesn't exist", thrown.getMessage());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la non suppression du mapping (address, station) de firestation (inexistant - null)
    void deleteFirestationMappingService_shouldThrowConflictExceptionWhenAddressAndStationNumberNull() {
        // Given
        String nullAddress = null;
        Integer nullStationNumber = null;

        // When / Then
        ConflictException thrown = assertThrows(ConflictException.class, () -> firestationService.deleteFirestationMappingService(nullAddress, nullStationNumber));
        assertEquals("Both address and station number can't be null", thrown.getMessage());
    }

    // TODO : Comprendre pourquoi ce test ne fonctionne pas.
//    @Test
//    // On va vérifier ici le bon fonctionnement de la récupération de la liste des personnes par station (valide)
//    void getPersonsByStationNumberService_shouldReturnPersonsByStation() {
//        // Given
//        Integer stationNumber = 1;
//        List<Firestation> testFirestationList = Arrays.asList(
//                new Firestation(" == DataAddressTest 1 == ", 1),
//                new Firestation(" == DataAddressTest 1bis == ", 1)
//        );
//
//        // Simuler les adresses des stations pour la station 1
//        Set<String> stationAddresses = Set.of(" == DataAddressTest 1 == ", " == DataAddressTest 1bis == ");
//        when(firestationRepository.getFirestationsByStation(stationNumber)).thenReturn(testFirestationList);
//
//        // Simuler les personnes par adresses - ajuster avec l'ensemble des adresses
//        doReturn(List.of(personList.get(0), personList.get(1)))
//                //.when(personRepository).getPersonsByAddress(stationAddresses);
//                .when(personRepository).getPersonsByAddress(anySet());
//
////        // Simuler les personnes par adresses
////        when(personRepository.getPersonsByAddress(" == DataAddressTest 1 == ")).thenReturn(List.of());
////        when(personRepository.getPersonsByAddress(" == DataAddressTest 1bis == ")).thenReturn(List.of());
//
//        // Simuler les dossiers médicaux (1 mineur et 1 majeur)
//        when(medicalRecordRepository.findMedicalRecordById(personList.get(0).getId())).thenReturn(Optional.of(minorMedicalRecord));
//        when(medicalRecordRepository.findMedicalRecordById(personList.get(1).getId())).thenReturn(Optional.of(adultMedicalRecord));
//
//        // When
//        PersonsByStation result = firestationService.getPersonsByStationService(stationNumber);
//
//        // Then
//        assertEquals(personList, result.getPersons(), "The list of persons should match the expected list.");
//        assertEquals(1, result.getNbrOfMinors(), "The number of minors should be 1.");
//
//        // Vérifier que les méthodes du repository sont appelées le bon nombre de fois
//        verify(firestationRepository, times(1)).getFirestationsByStation(stationNumber);
//        verify(personRepository, times(1)).getPersonsByAddress(stationAddresses);
//        verify(medicalRecordRepository, times(2)).findMedicalRecordById(anyString());
//    }






}
