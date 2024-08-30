package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class FirestationRepositoryImpTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private FirestationRepositoryImpl firestationRepository;

    private List<Firestation> firestationList;

    @BeforeEach
    public void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création des données de test
        firestationList = new ArrayList<>();
        firestationList.add(new Firestation(" == Données de test 0 == ", 0));
        firestationList.add(new Firestation(" == Données de test 1 == ", 1));
        firestationList.add(new Firestation(" == Données de test 1bis == ", 1));
        firestationList.add(new Firestation(" == Données de test 200 == ", 200));

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setFireStations(firestationList);

        //Configure le comportement du mock
        when(dataReaderService.getData()).thenReturn(dataWrapperList);
    }


    @Test
    // On va vérifier ici que lorsque l'on demande la liste des casernes, on obtient bien 3 éléments.
    public void getFirestationsServiceTest() {

        List<Firestation> firestations = firestationRepository.getFirestations();
        assertThat(firestations).isNotEmpty();
        assertThat(firestations.size()).isEqualTo(4);

    }



    @Test
    // On va vérifier ici que lorsqu'on l'on demande si une adresse de caserne existe ou pas dans la liste.
    public void getFirestationByAddressServiceTest() {

        boolean exists = firestationRepository.getFirestationByAddress(" == Données de test 200 == ");
        assertThat(exists).isTrue();

        boolean notExist = firestationRepository.getFirestationByAddress(" == Adresse qui n'existe pas == ");
        assertThat(notExist).isFalse();

        boolean nullExist = firestationRepository.getFirestationByAddress(null);
        assertThat(nullExist).isFalse();

    }



    @Test
    // On va vérifier ici la mise à jour du numéro de Station pour une adresse de caserne.
    public void updateFirestationMappingTest() {

        Firestation targetFirestation = new Firestation(" == Données de test 0 == ", 100);
        Firestation unknownFirestation = new Firestation(" == unknown Firestation address == ", 50);
        boolean isUpdated = firestationRepository.updateFirestationMapping(targetFirestation);
        boolean isNotFound = firestationRepository.updateFirestationMapping(unknownFirestation);

        assertThat(isUpdated).isTrue();
        assertThat(firestationRepository.getFirestations().contains(targetFirestation));
        assertThat(isNotFound).isFalse();

    }


    @Test
    // On va vérifier ici
    //public boolean addFirestationMapping(Firestation newFirestation) {
    public void addFirestationMapping() {
        Firestation newFirestation = new Firestation(" == Données de test 20 == ", 20);
        boolean isAdded = firestationRepository.addFirestationMapping(newFirestation);

        assertThat(isAdded).isTrue();
        assertThat(firestationRepository.getFirestations().contains(newFirestation));

    }



    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une caserne avec une adresse
    public void deleteFirestationMappingByAddressTest() {

        boolean isDeleted = firestationRepository.deleteFirestationMappingByAddress(" == Données de test 0 == ");
        assertThat(isDeleted).isTrue();
        assertThat(firestationRepository.getFirestations()).hasSize(3);

        boolean isNotDeleted = firestationRepository.deleteFirestationMappingByAddress(" == unknown Firestation address == ");
        assertThat(isNotDeleted).isFalse();

    }


    @Test
    // On va vérifier ici le bon fonctionnement de la suppression d'une ou plusieurs casernes avec un numéro de station.
    public void deleteFirestationMappingByStationTest() {

        boolean isDeleted = firestationRepository.deleteFirestationMappingByStation(1);
        assertThat(isDeleted).isTrue();
        assertThat(firestationRepository.getFirestations()).hasSize(2);

        boolean isNotDeleted = firestationRepository.deleteFirestationMappingByStation(999);
        assertThat(isNotDeleted).isFalse();

    }



    @Test
    // On va vérifier ici que le bon fonctionnement de la liste des casernes en fonction de leur numéro de station.
    public void getFirestationsByStationTest() {

        List<Firestation> testFirestationList = firestationRepository.getFirestationsByStation(1);
        assertThat(testFirestationList).hasSize(2);
        assertThat(testFirestationList.get(0).getStation()).isEqualTo(1);
        assertThat(testFirestationList.get(0).getAddress()).isEqualTo(" == Données de test 1 == ");

        List<Firestation> testFirestationEmptyList = firestationRepository.getFirestationsByStation(99);
        assertThat(testFirestationEmptyList).isEmpty();
    }


}
