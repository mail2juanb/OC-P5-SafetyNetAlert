package com.oc_P5.SafetyNetAlerts.person;

import com.oc_P5.SafetyNetAlerts.controller.PersonController;
import com.oc_P5.SafetyNetAlerts.controller.requests.PersonRequest;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    @Mock
    PersonServiceImpl personService;

    @InjectMocks
    PersonController personController;



    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void addPerson_shouldReturnResponseEntity() {
        // Given request and expected person object
        final PersonRequest addPersonRequest = new PersonRequest("newFirstNameTest1", "newLastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "email@Test1");
        final Person personExpected = new Person("newFirstNameTest1", "newLastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "email@Test1");

        doNothing().when(personService).addPerson(any());

        // When Person is post
        ResponseEntity<String> response = personController.addPerson(addPersonRequest);

        // Then verify that the service method was called with the expected person data and check the response status
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personService).addPerson(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue().getFirstName()).isEqualTo(personExpected.getFirstName());
        assertThat(personArgumentCaptor.getValue().getLastName()).isEqualTo(personExpected.getLastName());
        assertThat(personArgumentCaptor.getValue().getAddress()).isEqualTo(personExpected.getAddress());
        assertThat(personArgumentCaptor.getValue().getCity()).isEqualTo(personExpected.getCity());
        assertThat(personArgumentCaptor.getValue().getZip()).isEqualTo(personExpected.getZip());
        assertThat(personArgumentCaptor.getValue().getPhone()).isEqualTo(personExpected.getPhone());
        assertThat(personArgumentCaptor.getValue().getEmail()).isEqualTo(personExpected.getEmail());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void updatePerson_shouldReturnResponseEntity() {
        // Given request and expected person object
        PersonRequest updatePersonRequest = new PersonRequest("firstNameTest1", "lastNameTest1", "updateAddressTest1", "updateCityTest1", 1111, "123-456-7891", "emailTest1");
        Person personExpected = new Person("firstNameTest1", "lastNameTest1", "updateAddressTest1", "updateCityTest1", 1111, "123-456-7891", "emailTest1");

        doNothing().when(personService).updatePerson(any());

        // When Person is updated
        ResponseEntity<Void> response = personController.updatePerson(updatePersonRequest);

        // Then verify that the service method was called with the expected person data and check the response status
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personService).updatePerson(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue().getFirstName()).isEqualTo(personExpected.getFirstName());
        assertThat(personArgumentCaptor.getValue().getLastName()).isEqualTo(personExpected.getLastName());
        assertThat(personArgumentCaptor.getValue().getAddress()).isEqualTo(personExpected.getAddress());
        assertThat(personArgumentCaptor.getValue().getCity()).isEqualTo(personExpected.getCity());
        assertThat(personArgumentCaptor.getValue().getZip()).isEqualTo(personExpected.getZip());
        assertThat(personArgumentCaptor.getValue().getPhone()).isEqualTo(personExpected.getPhone());
        assertThat(personArgumentCaptor.getValue().getEmail()).isEqualTo(personExpected.getEmail());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
     // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void deletePerson_shouldReturnResponseEntity() {
        // Given request and expected person object
        PersonRequest deletePersonRequest = new PersonRequest("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "emailTest1");
        Person personExpected = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "emailTest1");
        doNothing().when(personService).deletePerson(any());

        // When Person is deleted
        ResponseEntity<Void> response = personController.deleteFirestation(deletePersonRequest);

        // Then verify that the service method was called with the expected person data and check the response status
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personService).deletePerson(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue().getFirstName()).isEqualTo(personExpected.getFirstName());
        assertThat(personArgumentCaptor.getValue().getLastName()).isEqualTo(personExpected.getLastName());
        assertThat(personArgumentCaptor.getValue().getAddress()).isEqualTo(personExpected.getAddress());
        assertThat(personArgumentCaptor.getValue().getCity()).isEqualTo(personExpected.getCity());
        assertThat(personArgumentCaptor.getValue().getZip()).isEqualTo(personExpected.getZip());
        assertThat(personArgumentCaptor.getValue().getPhone()).isEqualTo(personExpected.getPhone());
        assertThat(personArgumentCaptor.getValue().getEmail()).isEqualTo(personExpected.getEmail());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}