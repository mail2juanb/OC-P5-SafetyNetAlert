package com.oc_P5.SafetyNetAlerts.firestation;

import com.oc_P5.SafetyNetAlerts.controller.FirestationController;
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
    void getPersonsByStation_shouldReturnPersonsByStation() {
        // Given a stationNumber
        final Integer stationNumber = 1;

        // Given a list of Person
        final Person person = new Person("firstName", "lastName", "address", "city", 1111, "phone", "email");
        final List<Person> personList = new ArrayList<>();
        personList.add(person);

        // Given number of Minors
        final Integer nbrOfMinors = 0;

        // Given a PersonsByStation
        final PersonsByStation personsByStation = new PersonsByStation(personList, nbrOfMinors);

        when(firestationService.getPersonsByStation(stationNumber)).thenReturn(personsByStation);

        // When method is called
        ResponseEntity<PersonsByStation> result = firestationController.getPersonsByStation(stationNumber);

        // Then station is sent to service and check HttpStatus.OK
        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationService).getPersonsByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(stationNumber);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void addFirestation_shouldReturnResponseEntity() {

        // Given a Firestation to add
        final FirestationRequest addFirestationRequest = new FirestationRequest("addAddress", 9);
        final Firestation expectedFirestation = new Firestation("addAddress", 9);
        doNothing().when(firestationService).addFirestation(any());

        // When Firestation is post
        ResponseEntity<?> response = firestationController.addFirestation(addFirestationRequest);

        // Then Firestation is sent to the service and check HttpStatus.CREATED
        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationService).addFirestation(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(expectedFirestation);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updateFirestation_shouldReturnResponseEntity() {
        // Given a Firestation to update
        final FirestationRequest updateFirestationRequest = new FirestationRequest("addressTest1", 9);
        final Firestation expectedFirestation = new Firestation("addressTest1", 9);
        doNothing().when(firestationService).updateFirestation(any());

        // When Firestation is updated
        ResponseEntity<?> response = firestationController.updateFirestation(updateFirestationRequest);

        // Then Firestation is sent to the service  and check HttpStatus.OK
        ArgumentCaptor<Firestation> firestationArgumentCaptor = ArgumentCaptor.forClass(Firestation.class);
        verify(firestationService).updateFirestation(firestationArgumentCaptor.capture());
        assertThat(firestationArgumentCaptor.getValue()).isEqualTo(expectedFirestation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteFirestationByAddress_shouldReturnResponseEntity() {
        // Given an address of Firestation to delete
        final String address = "addressTest1";
        doNothing().when(firestationService).deleteFirestationByAddress(address);

        // When Firestation is deleted
        ResponseEntity<?> response = firestationController.deleteFirestationByAddress(address);

        // Then address is sent to the service and check HttpStatus.OK
        ArgumentCaptor<String> addressArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(firestationService).deleteFirestationByAddress(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteFirestationByStation_shouldReturnResponseEntity() {
        // Given a station of Firestation to delete
        final Integer station_number = 1;
        doNothing().when(firestationService).deleteFirestationByStation(station_number);

        // When Firestation is deleted
        ResponseEntity<?> response = firestationController.deleteFirestationByStation(station_number);

        // Then station is sent to the service and check HttpStatus.OK
        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationService).deleteFirestationByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(station_number);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}