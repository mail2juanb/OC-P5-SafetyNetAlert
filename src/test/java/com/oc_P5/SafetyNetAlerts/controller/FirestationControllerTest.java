package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.FirestationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

    @Mock
    FirestationServiceImpl firestationService;

    @InjectMocks
    FirestationController firestationController;

    private List<Firestation> firestationListMock;


    @BeforeEach
    public void setUp() {
        Firestation firestation1 = new Firestation();
        firestation1.setAddress("addressTest1");
        firestation1.setStation(1);

        firestationListMock = new ArrayList<>();
        firestationListMock.add(firestation1);
    }



    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getFirestations_shouldReturnListOfFirestation() {
        // Given
        when(firestationService.getFirestations()).thenReturn(firestationListMock);

        // When
        List<Firestation> result = firestationController.getFirestations();

        // Then
        verify(firestationService, times(1)).getFirestations();
        assertEquals(firestationListMock, result);
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void addFirestation_shouldReturnResponseEntity() {
        Firestation firestation = new Firestation();
        firestation.setAddress("addAddress");
        firestation.setStation(9);
        // Given
        doNothing().when(firestationService).addFirestation(firestation);

        // When
        ResponseEntity<String> response = firestationController.addFirestation(firestation);

        // Then
        verify(firestationService, times(1)).addFirestation(firestation);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void updateFirestation_shouldReturnResponseEntity() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("addressTest1");
        firestation.setStation(9);
        doNothing().when(firestationService).updateFirestation(firestation);

        // When
        ResponseEntity<Void> response = firestationController.updateFirestation(firestation);

        // Then
        verify(firestationService, times(1)).updateFirestation(firestation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void deleteFirestation_shouldReturnResponseEntity() {
        // Given
        Firestation firestation = new Firestation();
        firestation.setAddress("addressTest1");
        firestation.setStation(1);
        doNothing().when(firestationService).deleteFirestation(firestation);

        // When
        ResponseEntity<Void> response = firestationController.deleteFirestation(firestation);

        // Then
        verify(firestationService, times(1)).deleteFirestation(firestation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est bien appellée ainsi que les bons arguments
    void getPersonsByStation_shouldReturnPersonsByStation() {
        // Given
        Integer stationNumber = 1;
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setPhone("phone");

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        Integer nbrOfMinors = 0;

        PersonsByStation personsByStation = new PersonsByStation(personList, nbrOfMinors);
        when(firestationService.getPersonsByStation(stationNumber)).thenReturn(personsByStation);

        // When
        PersonsByStation result = firestationController.getPersonsByStation(stationNumber);

        // Then
        verify(firestationService, times(1)).getPersonsByStation(stationNumber);
        assertEquals(personsByStation, result);
    }

}