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
public class FirestationRepositoryTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private FirestationRepositoryImpl firestationRepository;

    private List<Firestation> firestationListMock;

    @BeforeEach
    public void setUp() {
        // NOTE Initialisation des mocks fait par l'annotation @ExtendWith(MockitoExtension.class)

        // Création des données de test
        Firestation firestation1 = new Firestation();
        firestation1.setAddress("addressTest1");
        firestation1.setStation(1);

        Firestation firestation2 = new Firestation();
        firestation2.setAddress("addressTest2");
        firestation2.setStation(2);

        Firestation firestation1b = new Firestation();
        firestation1b.setAddress("addressTest1b");
        firestation1b.setStation(1);

        firestationListMock = new ArrayList<>();
        firestationListMock.add(firestation1);
        firestationListMock.add(firestation1b);
        firestationListMock.add(firestation2);

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setFireStations(firestationListMock);

        //Configure le comportement du mock
        when(dataReaderService.getData()).thenReturn(dataWrapperList);
    }


    @Test
    // On va vérifier ici que la méthode retourne bien les données mock
    void getFirestations_shouldReturnListOfFirestations() {
        // When
        List<Firestation> firestationList = firestationRepository.getAll();

        // Then
        assertEquals(3, firestationList.size());
        assertTrue(firestationList.containsAll(firestationListMock));
        assertEquals("addressTest1", firestationList.get(0).getAddress());
        assertEquals("addressTest1b", firestationList.get(1).getAddress());
        assertEquals("addressTest2", firestationList.get(2).getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation (address, stationNumber) connue
    void findByAddressByStation_shouldReturnFirestation() {
        // Given
        Firestation firestation = new Firestation("addressTest1", 1);

        // When
        Optional<Firestation> result = firestationRepository.findByAddressByStation(firestation);

        // Then
        assertTrue(result.isPresent());
        assertEquals(firestation.getAddress(), result.get().getAddress());
        assertEquals(firestation.getStation(), result.get().getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation (address, stationNumber) inconnue
    void findByAddressByStation_shouldReturnFirestationEmpty() {
        // Given
        Firestation firestation = new Firestation("addressNotExists", 20);

        // When
        Optional<Firestation> result = firestationRepository.findByAddressByStation(firestation);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address, stationNumber) connue
    void existsByAddressByStation_shouldReturnTrue() {
        // Given
        Firestation firestation = new Firestation("addressTest1", 1);

        // When
        boolean exists = firestationRepository.existsByAddressByStation(firestation);

        // Then
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address, stationNumber) inconnue
    void existsByAddressByStation_shouldReturnFalse() {
        // Given
        Firestation firestation = new Firestation("addressNotExists", 20);

        // When
        boolean exists = firestationRepository.existsByAddressByStation(firestation);

        // Then
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) connue
    void findByAddress_shouldReturnFirestation() {
        // Given
        String address = "addressTest1";

        // When
        Optional<Firestation> result = firestationRepository.findByAddress(address);

        // Then
        assertTrue(result.isPresent());
        assertEquals(address, result.get().getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) inconnue
    void findByAddress_shouldReturnFirestationEmpty() {
        // Given
        String address = "addressNotExists";

        // When
        Optional<Firestation> result = firestationRepository.findByAddress(address);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) connue
    void existsByAddress_shouldReturnTrue() {
        // Given
        String address = "addressTest1";

        // When
        boolean exists = firestationRepository.existsByAddress(address);

        // Then
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (address) inconnue
    void existsByAddress_shouldReturnFalse() {
        // Given
        String address = "addressNotExists";

        // When
        boolean exists = firestationRepository.existsByAddress(address);

        // Then
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (station) connue
    void findByStation_shouldReturnFirestation() {
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
    void findByStation_shouldReturnFirestationEmpty() {
        // Given
        Integer stationNumber = 999;

        // When
        Optional<Firestation> result = firestationRepository.findByStation(stationNumber);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (station) connue
    void existsByStation_shouldReturnTrue() {
        // Given
        Integer stationNumber = 1;

        // When
        boolean exists = firestationRepository.existsByStation(stationNumber);

        // Then
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation (station) inconnue
    void existsByStation_shouldReturnFalse() {
        // Given
        Integer stationNumber = 999;

        // When
        boolean exists = firestationRepository.existsByStation(stationNumber);

        // Then
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici qu'avec un objet Firestation dont l'adresse est connue, on met à jour le numéro de station
    void updateFirestation_shouldUpdate() {
        // Given
        Firestation firestation = new Firestation("addressTest1", 199);

        // When
        firestationRepository.update(firestation);

        // Then
        assertEquals(firestation.getAddress(), firestationListMock.getFirst().getAddress());
        assertEquals(firestation.getStation(), firestationListMock.getFirst().getStation());
    }


    @Test
    // On va vérifier ici le bon fonctionnement de l'ajout d'une Firestation
    void saveFirestation_shouldSave() {
        // Given
        Firestation firestation = new Firestation("addressTest10", 10);

        // When
        firestationRepository.save(firestation);

        // Then
        assertEquals(4, firestationListMock.size());
        assertTrue(firestationListMock.contains(firestation));
        assertEquals(firestation.getAddress(), firestationListMock.get(3).getAddress());
        assertEquals(firestation.getStation(), firestationListMock.get(3).getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une Firestation connue
    void deleteFirestation_shouldDelete() {
        // Given
        Firestation firestation = new Firestation("addressTest1", 1);

        // When
        firestationRepository.delete(firestation);

        // Then
        assertFalse(firestationListMock.contains(firestation));
        assertEquals(2, firestationListMock.size());
        assertEquals("addressTest1b", firestationListMock.get(0).getAddress());
        assertEquals("addressTest2", firestationListMock.get(1).getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une Firestation avec une adresse
    void deleteByAddress_shouldRemoveFirestation() {
        // Given
        String address = "addressTest2";

        // When
        firestationRepository.deleteByAddress(address);

        // Then
        assertFalse(firestationListMock.stream().anyMatch(firestation -> firestation.getAddress().equals(address)));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une ou plusieurs Firestations avec un numéro de station connu
    void deleteByStation_shouldDeleteFirestations() {
        // Given
        Integer stationNumber = 1;

        // When
        firestationRepository.deleteByStation(stationNumber);

        // Then
        assertFalse(firestationListMock.stream().anyMatch(firestation -> firestation.getStation().equals(stationNumber)));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Firestation par station
    void getByStation_shouldReturnListOfFirestation() {
        // Given
        Integer stationNumber = 1;

        // When
        List<Firestation> resultFirestationList = firestationRepository.getByStation(stationNumber);

        // Then
        assertEquals(2, resultFirestationList.size());
        assertTrue(resultFirestationList.stream().allMatch(firestation -> firestation.getStation().equals(stationNumber)));
    }


}
