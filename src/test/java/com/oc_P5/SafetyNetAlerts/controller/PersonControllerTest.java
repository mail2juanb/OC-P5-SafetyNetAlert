package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.PersonServiceImpl;
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
public class PersonControllerTest {

    @Mock
    PersonServiceImpl personService;

    @InjectMocks
    PersonController personController;

    private List<Person> personListMock;


    @BeforeEach
    public void setUp() {
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "emailTest1");
        Person person2 = new Person("firstNameTest2", "lastNameTest2", "addressTest2", "cityTest2", 2222, "123-456-7892", "emailTest2");
        personListMock = new ArrayList<>();
        personListMock.add(person1);
        personListMock.add(person2);
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getPersons_shouldReturnListOfPerson() {
        // Given
        when(personService.getPersons()).thenReturn(personListMock);

        // When
        List<Person> result = personController.getPersons();

        // Then
        verify(personService, times(1)).getPersons();
        assertEquals(personListMock, result);
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void addPerson_shouldReturnResponseEntity() {
        // Given
        Person person = new Person("newFirstNameTest1", "newLastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "emailTest1");
        doNothing().when(personService).addPerson(person);

        // When
        ResponseEntity<String> response = personController.addPerson(person);

        // Then
        verify(personService, times(1)).addPerson(person);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void updatePerson_shouldReturnResponseEntity() {
        // Given
        Person person = new Person("firstNameTest1", "lastNameTest1", "updateAddressTest1", "updateCityTest1", 1111, "123-456-7891", "emailTest1");
        doNothing().when(personService).updatePerson(person);

        // When
        ResponseEntity<Void> response = personController.updatePerson(person);

        // Then
        verify(personService, times(1)).updatePerson(person);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
     // On va vérifier ici que la méthode du service est déclenchée et que le code de réponse est correct
    void deletePerson_shouldReturnResponseEntity() {
        // Given
        Person person = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "emailTest1");
        doNothing().when(personService).deletePerson(person);

        // When
        ResponseEntity<Void> response = personController.deleteFirestation(person);

        // Then
        verify(personService, times(1)).deletePerson(person);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
