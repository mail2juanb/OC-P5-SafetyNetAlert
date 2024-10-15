package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.controller.requests.FirestationRequest;
import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.FirestationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

    @Mock
    FirestationServiceImpl firestationService;

    @InjectMocks
    FirestationController firestationController;


    @Test
    // On va vérifier ici que la méthode du service est bien appelée ainsi que les bons arguments
    void getPersonsByStation_shouldReturnPersonsByStation() {
        // Given
        final Integer station_number = 1;

        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setAddress("address");
        person.setPhone("phone");

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        final Integer nbrOfMinors = 0;

        PersonsByStation personsByStation = new PersonsByStation(personList, nbrOfMinors);
        when(firestationService.getPersonsByStation(station_number)).thenReturn(personsByStation);

        // When method is called
        ResponseEntity<PersonsByStation> result = firestationController.getPersonsByStation(station_number);

        // Then station is sent to service
        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationService).getPersonsByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(station_number);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void addFirestation_shouldReturnResponseEntity() {

        // Given a Firestation to add
        final FirestationRequest addFirestationRequest = new FirestationRequest("addAddress", 9);
        final Firestation expectedFirestation = new Firestation("addAddress", 9);
        doNothing().when(firestationService).addFirestation(any());

        // When Firestation is post
        ResponseEntity<String> response = firestationController.addFirestation(addFirestationRequest);

        // Then Firestation is sent to the service
        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationService).addFirestation(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(expectedFirestation);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void updateFirestation_shouldReturnResponseEntity() {
        // Given a Firestation to update
        final FirestationRequest updateFirestationRequest = new FirestationRequest("addressTest1", 9);
        final Firestation expectedFirestation = new Firestation("addressTest1", 9);
        doNothing().when(firestationService).updateFirestation(any());

        // When Firestation is updated
        ResponseEntity<Void> response = firestationController.updateFirestation(updateFirestationRequest);

        // Then Firestation is sent to the service
        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationService).updateFirestation(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(expectedFirestation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void deleteFirestationByAddress_shouldReturnResponseEntity() {
        // Given an address of Firestation to delete
        final String address = "addressTest1";
        doNothing().when(firestationService).deleteFirestationByAddress(address);

        // When Firestation is deleted
        ResponseEntity<Void> response = firestationController.deleteFirestationByAddress(address);

        // Then address is sent to the service
        ArgumentCaptor<String> addressArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(firestationService).deleteFirestationByAddress(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void deleteFirestationByStation_shouldReturnResponseEntity() {
        // Given a station of Firestation to delete
        final Integer station_number = 1;
        doNothing().when(firestationService).deleteFirestationByStation(station_number);

        // When Firestation is deleted
        ResponseEntity<Void> response = firestationController.deleteFirestationByStation(station_number);

        // Then station is sent to the service
        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationService).deleteFirestationByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(station_number);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}