package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryImplTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private FirestationRepositoryImpl firestationRepository;

    private List<Firestation> firestationList;

    @BeforeEach
    public void setUp() {
        // NOTE Initialisation des mocks fait par l'annotation @ExtendWith(MockitoExtension.class)

        // Création des données de test
        firestationList = new ArrayList<>();
        firestationList.add(new Firestation(" == DataTest 0 == ", 0));
        firestationList.add(new Firestation(" == DataTest 1 == ", 1));
        firestationList.add(new Firestation(" == DataTest 1bis == ", 1));
        firestationList.add(new Firestation(" == DataTest 200 == ", 200));

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setFireStations(firestationList);

        //Configure le comportement du mock
        when(dataReaderService.getData()).thenReturn(dataWrapperList);
    }


    @Test
    // On va vérifier ici que la méthode retourne bien l'ensemble des données pré-chargées
    void getFirestations_shouldReturnListOfFirestations() {
        // When
        List<Firestation> resultList = firestationRepository.getFirestations();

        // Then
        assertEquals(firestationList.size(), resultList.size());
        assertTrue(resultList.containsAll(firestationList));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation (address, stationNumber) connue
    void findByAddressByStation_shouldReturnWhenExists() {
        // Given
        Firestation searchFirestation = new Firestation(" == DataTest 1 == ", 1);

        // When
        Optional<Firestation> result = firestationRepository.findByAddressByStation(searchFirestation);

        // Then
        assertTrue(result.isPresent());
        assertEquals(searchFirestation.getAddress(), result.get().getAddress());
        assertEquals(searchFirestation.getStation(), result.get().getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation (address, stationNumber) inconnue
    void findByAddressByStation_shouldReturnEmptyWhenNotExists() {
        // Given
        Firestation searchFirestation = new Firestation(" == DataTest Unknown == ", 20);

        // When
        Optional<Firestation> result = firestationRepository.findByAddressByStation(searchFirestation);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address, stationNumber) connue
    void isExists_shouldReturnTrueWhenExistsByAddressByStation() {
        // Given
        Firestation searchFirestation = new Firestation(" == DataTest 1 == ", 1);

        // When
        boolean exists = firestationRepository.existsByAddressByStation(searchFirestation);

        // Then
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address, stationNumber) inconnue
    void isExists_shouldReturnFalseWhenNotExistsByAddressByStation() {
        // Given
        Firestation searchFirestation = new Firestation(" == DataTest Unknown == ", 20);

        // When
        boolean exists = firestationRepository.existsByAddressByStation(searchFirestation);

        // Then
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) connue
    void findFirestationByAddress_shouldReturnWhenExists() {
        // Given
        String searchFirestationAddress = " == DataTest 1 == ";

        // When
        Optional<Firestation> result = firestationRepository.findByAddress(searchFirestationAddress);

        // Then
        assertTrue(result.isPresent());
        assertEquals(searchFirestationAddress, result.get().getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) inconnue
    void findByAddress_shouldReturnEmptyWhenNotExists() {
        // Given
        String searchFirestationAddress = " == DataTest Unknown == ";

        // When
        Optional<Firestation> result = firestationRepository.findByAddress(searchFirestationAddress);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) connue
    void isExists_shouldReturnTrueWhenExistsByAddress() {
        // Given
        String address = " == DataTest 1 == ";

        // When
        boolean exists = firestationRepository.existsByAddress(address);

        // Then
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) inconnue
    void isExists_shouldReturnFalseWhenNotExistsByAddress() {
        // Given
        String address = " == DataTest Unknown == ";

        // When
        boolean exists = firestationRepository.existsByAddress(address);

        // Then
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (station) connue
    void findFirestationByStation_shouldReturnWhenExists() {
        // Given
        Integer stationNumber = 1;

        // When
        Optional<Firestation> result = firestationRepository.findByStation(stationNumber);

        // Then
        assertTrue(result.isPresent());
        assertEquals(stationNumber, result.get().getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (station) inconnue
    void findByStation_shouldReturnEmptyWhenNotExists() {
        // Given
        Integer stationNumber = 999;

        // When
        Optional<Firestation> result = firestationRepository.findByStation(stationNumber);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (station) connue
    void isExists_shouldReturnTrueWhenExistsByStation() {
        // Given
        Integer stationNumber = 1;

        // When
        boolean exists = firestationRepository.existsByStation(stationNumber);

        // Then
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (station) inconnue
    void isExists_shouldReturnFalseWhenNotExistsByStation() {
        // Given
        Integer stationNumber = 999;

        // When
        boolean exists = firestationRepository.existsByStation(stationNumber);

        // Then
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici qu'avec un objet Firestation dont l'adresse est connue, on met à jour le numéro de station
    void updateFirestationMapping_shouldUpdateFirestationWhenExists() {
        // Given
        Firestation updateFirestation = new Firestation(" == DataTest 1 == ", 199);

        // When
        Optional<Firestation> result = firestationRepository.update(updateFirestation);

        // Then
        assertTrue(result.isPresent());
        assertEquals(199, result.get().getStation());
    }

    @Test
    // On va vérifier ici qu'avec un objet Firestation dont l'adresse est inconnue, on ne met pas à jour le numéro de station
    void update_shouldReturnEmptyWhenNotExists() {
        // Given
        Firestation updateFirestation = new Firestation(" == DataTest Unknown == ", 199);

        // When
        Optional<Firestation> result = firestationRepository.update(updateFirestation);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de l'ajout d'une Firestation
    void addFirestationMapping_shouldAddNewFirestation() {
        // Given
        Firestation createFirestation = new Firestation(" == DataTest 10 == ", 10);

        // When
        firestationRepository.save(createFirestation);
        List<Firestation> resultList = firestationRepository.getFirestations();

        // Then
        assertTrue(resultList.contains(createFirestation));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une Firestation connue
    void deleteFirestationMapping_shouldRemoveFirestationWhenExists() {
        // Given
        Firestation deleteFirestation = new Firestation(" == DataTest 1 == ", 1);

        // When
        firestationRepository.delete(deleteFirestation);
        List<Firestation> resultList = firestationRepository.getFirestations();

        // Then
        assertFalse(resultList.contains(deleteFirestation));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une Firestation inconnue
    void deleteFirestationMapping_shouldNotRemoveFirestationWhenNotExists() {
        // Given
        Firestation deleteFirestation = new Firestation(" == DataTest Unknown == ", 999);
        List<Firestation> originFirestationList = firestationRepository.getFirestations();

        // When
        firestationRepository.delete(deleteFirestation);
        List<Firestation> resultFirestationList = firestationRepository.getFirestations();

        // Then
        assertEquals(originFirestationList.size(), resultFirestationList.size());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une Firestation avec une adresse connue
    void deleteFirestationMappingByAddress_shouldRemoveFirestationWhenExists() {
        // Given
        String deleteAddress = " == DataTest 200 == ";

        // When
        firestationRepository.deleteByAddress(deleteAddress);
        List<Firestation> resultList = firestationRepository.getFirestations();

        // Then
        assertFalse(resultList.stream().anyMatch(firestation -> firestation.getAddress().equals(deleteAddress)));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la non suppression d'une Firestation avec une adresse inconnue
    void deleteByAddress_shouldNotRemoveWhenAddressNotExists() {
        // Given
        String deleteAddress = " == DataTest Unknown == ";
        List<Firestation> originFirestationList = firestationRepository.getFirestations();

        // When
        firestationRepository.deleteByAddress(deleteAddress);
        List<Firestation> resultFirestationList = firestationRepository.getFirestations();

        // Then
        assertEquals(originFirestationList.size(), resultFirestationList.size());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une ou plusieurs Firestations avec un numéro de station connu
    void deleteFirestationMappingByStation_shouldRemoveFirestationWhenExists() {
        // Given
        Integer deleteStationNumber = 1;

        // When
        firestationRepository.deleteByStation(deleteStationNumber);
        List<Firestation> resultFirestationList = firestationRepository.getFirestations();

        // Then
        assertFalse(resultFirestationList.stream().anyMatch(firestation -> firestation.getStation().equals(deleteStationNumber)));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la non suppression d'une ou plusieurs Firestations avec un numéro de station inconnu
    void deleteByStation_shouldNotRemoveWhenStationNotExists() {
        // Given
        Integer deleteStationNumber = 999;
        List<Firestation> originalFirestationList = firestationRepository.getFirestations();

        // When
        firestationRepository.deleteByStation(deleteStationNumber);
        List<Firestation> resultFirestationList = firestationRepository.getFirestations();

        // Then
        assertEquals(originalFirestationList.size(), resultFirestationList.size());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Firestation par station
    // Ainsi que le nombre d'adultes et d'enfants ...
    void getFirestationsByStation_shouldReturnListOfByStation() {
        // Given
        Integer stationNumber = 1;

        // When
        List<Firestation> resultFirestationList = firestationRepository.getByStation(stationNumber);

        // Then
        assertEquals(2, resultFirestationList.size());
        assertTrue(resultFirestationList.stream().allMatch(firestation -> firestation.getStation().equals(stationNumber)));
    }

}
