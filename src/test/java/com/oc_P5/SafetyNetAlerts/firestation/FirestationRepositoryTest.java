package com.oc_P5.SafetyNetAlerts.firestation;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepositoryImpl;
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

        // NOTE Test data creation
        Firestation firestation1 = new Firestation("addressTest1", 1);
        Firestation firestation2 = new Firestation("addressTest2", 2);
        Firestation firestation1b = new Firestation("addressTest1b", 1);

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
    void getAll_shouldReturnListOfFirestations() {
        // When call method on repository
        List<Firestation> firestationList = firestationRepository.getAll();

        // Then verify that the returned list contains expected values
        assertEquals(3, firestationList.size());
        assertTrue(firestationList.containsAll(firestationListMock));
        assertEquals("addressTest1", firestationList.get(0).getAddress());
        assertEquals("addressTest1b", firestationList.get(1).getAddress());
        assertEquals("addressTest2", firestationList.get(2).getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation par address et par station_number connue
    void findByAddressByStation_shouldReturnFirestation() {
        // Given a known Firestation
        Firestation firestation = new Firestation("addressTest1", 1);

        // When call method on repository
        Optional<Firestation> result = firestationRepository.findByAddressByStation(firestation);

        // Then verify that the returned object contains expected values
        assertTrue(result.isPresent());
        assertEquals(firestation.getAddress(), result.get().getAddress());
        assertEquals(firestation.getStation(), result.get().getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation par address et par station_number inconnue
    void findByAddressByStation_shouldReturnFirestationEmpty() {
        // Given an unknown Firestation
        Firestation firestation = new Firestation("addressNotExists", 20);

        // When call method on repository
        Optional<Firestation> result = firestationRepository.findByAddressByStation(firestation);

        // Then verify that object returned is empty
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation connue
    void existsByAddressByStation_shouldReturnTrue() {
        // Given a known Firestation
        Firestation firestation = new Firestation("addressTest1", 1);

        // When call method on repository
        boolean exists = firestationRepository.existsByAddressByStation(firestation);

        // Then verify that response is true
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une Firestation inconnue
    void existsByAddressByStation_shouldReturnFalse() {
        // Given an unknown Firestation
        Firestation firestation = new Firestation("addressNotExists", 20);

        // When call method on repository
        boolean exists = firestationRepository.existsByAddressByStation(firestation);

        // Then verify that response is false
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation par address connue
    void findByAddress_shouldReturnFirestation() {
        // Given a known address
        String address = "addressTest1";

        // When call method on repository
        Optional<Firestation> result = firestationRepository.findByAddress(address);

        // Then verify that the returned object contains expected values
        assertTrue(result.isPresent());
        assertEquals(address, result.get().getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation par address inconnue
    void findByAddress_shouldReturnFirestationEmpty() {
        // Given an unknown address
        String address = "addressNotExists";

        // When call method on repository
        Optional<Firestation> result = firestationRepository.findByAddress(address);

        // Then verify that object returned is empty
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse avec une address connue
    void existsByAddress_shouldReturnTrue() {
        // Given a known address
        String address = "addressTest1";

        // When call method on repository
        boolean exists = firestationRepository.existsByAddress(address);

        // Then verify that response is true
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse avec une address inconnue
    void existsByAddress_shouldReturnFalse() {
        // Given an unknown address
        String address = "addressNotExists";

        // When call method on repository
        boolean exists = firestationRepository.existsByAddress(address);

        // Then verify that response is false
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation avec une station connue
    void findByStation_shouldReturnFirestation() {
        // Given a known station
        Integer station_number = 1;

        // When call method on repository
        Optional<Firestation> result = firestationRepository.findByStation(station_number);

        // Then verify that the returned object contains expected values
        assertTrue(result.isPresent());
        assertEquals(station_number, result.get().getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Firestation avec une station inconnue
    void findByStation_shouldReturnFirestationEmpty() {
        // Given an unknown station
        Integer station_number = 999;

        // When call method on repository
        Optional<Firestation> result = firestationRepository.findByStation(station_number);

        // Then verify that object returned is empty
        assertTrue(result.isEmpty());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse avec une station connue
    void existsByStation_shouldReturnTrue() {
        // Given a known station
        Integer station_number = 1;

        // When call method on repository
        boolean exists = firestationRepository.existsByStation(station_number);

        // Then verify that response is true
        assertTrue(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse avec une station inconnue
    void existsByStation_shouldReturnFalse() {
        // Given an unknown station
        Integer station_number = 999;

        // When call method on repository
        boolean exists = firestationRepository.existsByStation(station_number);

        // Then verify that response is false
        assertFalse(exists);
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la mise à jour d'une Firestation
    void updateFirestation_shouldUpdate() {
        // Given a Firestation to update
        Firestation firestation = new Firestation("addressTest1", 199);

        // When call method on repository
        firestationRepository.update(firestation);

        // Then verify that Firestation is updated
        assertEquals(3, firestationListMock.size());
        assertEquals(firestation.getAddress(), firestationListMock.getFirst().getAddress());
        assertEquals(firestation.getStation(), firestationListMock.getFirst().getStation());
    }


    @Test
    // On va vérifier ici le bon fonctionnement de l'ajout d'une Firestation
    void saveFirestation_shouldSave() {
        // Given a Firestation to save
        Firestation firestation = new Firestation("addressTest10", 10);

        // When call method on repository
        firestationRepository.save(firestation);

        // Then verify that Firestation is saved
        assertEquals(4, firestationListMock.size());
        assertTrue(firestationListMock.contains(firestation));
        assertEquals(firestation.getAddress(), firestationListMock.get(3).getAddress());
        assertEquals(firestation.getStation(), firestationListMock.get(3).getStation());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une Firestation avec une address
    void deleteByAddress_shouldRemoveFirestation() {
        // Given an address to delete
        String address = "addressTest2";

        // When call method on repository
        firestationRepository.deleteByAddress(address);

        // Then verify that Firestation is deleted
        assertEquals(2, firestationListMock.size());
        assertFalse(firestationListMock.stream().anyMatch(firestation -> firestation.getAddress().equals(address)));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une ou plusieurs Firestations avec un numéro de station connu
    void deleteByStation_shouldDeleteFirestations() {
        // Given a station_number to delete
        Integer station_number = 1;

        // When call method on repository
        firestationRepository.deleteByStation(station_number);

        // Then verify that Firestations are deleted
        assertEquals(1, firestationListMock.size());
        assertFalse(firestationListMock.stream().anyMatch(firestation -> firestation.getStation().equals(station_number)));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Firestation par station
    void getByStation_shouldReturnListOfFirestation() {
        // Given a station_number
        Integer station_number = 1;

        // When call method on repository
        List<Firestation> resultFirestationList = firestationRepository.getByStation(station_number);

        // Then verify that result is correct
        assertEquals(2, resultFirestationList.size());
        assertTrue(resultFirestationList.stream().allMatch(firestation -> firestation.getStation().equals(station_number)));
    }


}
