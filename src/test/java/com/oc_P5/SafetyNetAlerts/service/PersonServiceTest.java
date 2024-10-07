package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    PersonRepositoryImpl personRepository;

    @InjectMocks
    PersonServiceImpl personService;

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
    // On va vérifier ici que la méthode renvoi la liste des Person
    void getPersons_shouldReturnListOfPerson() {
        // Given
        when(personRepository.getAll()).thenReturn(personListMock);

        // When
        List<Person> result = personService.getPersons();

        // Then
        assertEquals(personListMock, result);
        verify(personRepository, times(1)).getAll();
    }

    @Test
    // On va vérifier ici que lorsqu'une Person est ajoutée, elle est correctement sauvegardée avec les bons attributs.
    void addPerson_ShouldAddPersonWhenNotExist() {
        // Given
        Person person = new Person("addFirstName", "addLastName", "addAddress", "addCity", 9999, "123-456-9999", "addEmail");

        when(personRepository.existsById(person.getId())).thenReturn(false);

        // When
        personService.addPerson(person);

        // Then
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(personArgumentCaptor.capture());

        Person savedPerson = personArgumentCaptor.getValue();
        assertThat(savedPerson).isEqualTo(person);

        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, times(1)).save(person);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPerson")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException
    void addPerson_shouldReturnNullOrEmptyObjectExceptionWithEmptyPerson(Person person) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> personService.addPerson(person));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
        verify(personRepository, never()).existsById(anyString());
        verify(personRepository, never()).save(person);
    }

    @Test
    // On va vérifier ici que la méthode lève une ConflictException lorsque la Person existe déjà
    void addPerson_shouldReturnConflictExceptionWhenExists() {
        // Given
        Person person = personListMock.getFirst();

        when(personRepository.existsById(person.getId())).thenReturn(true);

        // When / Then
        ConflictException thrown = assertThrows(ConflictException.class, () -> personService.addPerson(person));
        assertThat(thrown.getMessage()).contains(person.getFirstName());
        assertThat(thrown.getMessage()).contains(person.getLastName());

        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, never()).save(person);
    }

    @Test
    // On va vérifier ici que la Person est mise à jour avec une Person valide
    void updatePerson_shouldUpdatePersonWhenExists() {
        // Given
        Person person = new Person("firstNameTest1", "lastNameTest1", "newAddressTest1", "newCityTest1", 1111, "123-456-7891", "emailTest1");

        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        // When
        personService.updatePerson(person);

        // Then
        verify(personRepository, times(1)).findById(person.getId());
        verify(personRepository, times(1)).update(person);

        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).update(personArgumentCaptor.capture());
        Person updatedPerson = personArgumentCaptor.getValue();
        assertThat(updatedPerson)
                .isNotNull()
                .satisfies(p -> {
                    assertThat(p.getFirstName()).isEqualTo(person.getFirstName());
                    assertThat(p.getLastName()).isEqualTo(person.getLastName());
                    assertThat(p.getAddress()).isEqualTo(person.getAddress());
                    assertThat(p.getCity()).isEqualTo(person.getCity());
                });
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPerson")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException
    void updatePerson_shouldReturnNullOrEmptyObjectExceptionWithEmptyPerson(Person person) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> personService.updatePerson(person));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
        verify(personRepository, never()).findById(anyString());
        verify(personRepository, never()).update(person);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque la Person n'existe pas
    void updatePerson_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given
        Person person = new Person("notExistFirstName", "notExistLastName", "notExistAddress", "notExistCity", 1111, "123-456-7891", "emailTest1");

        when(personRepository.findById(person.getId())).thenReturn(Optional.empty());

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> personService.updatePerson(person));
        assertThat(thrown.getMessage()).contains(person.getId());

        verify(personRepository, times(1)).findById(person.getId());
        verify(personRepository, never()).update(any(Person.class));
    }

    @Test
    // On va vérifier ici que le MedicalRecord est supprimé avec une Person valide
    void deleteMedicalRecord_shouldDeleteMedicalRecord() {
        // Given
        Person person = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "emailTest1");

        when(personRepository.existsById(person.getId())).thenReturn(true);

        // When
        personService.deletePerson(person);

        // Then
        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, times(1)).delete(person);

        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).delete(personArgumentCaptor.capture());
        Person deletedPerson = personArgumentCaptor.getValue();
        assertThat(deletedPerson).isEqualTo(person);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPerson")
        // On va vérifier ici que la méthode lève une NullOrEmptyObjectException
    void deletePerson_shouldReturnNullOrEmptyObjectExceptionWithEmptyPerson(Person person) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> personService.deletePerson(person));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
        verify(personRepository, never()).findById(anyString());
        verify(personRepository, never()).delete(person);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque la Person n'existe pas
    void deletePerson_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given
        Person person = new Person("unknownFirstName", "unknownLastName", "unknownAddress", "cityTest1", 1111, "123-456-7891", "emailTest1");

        when(personRepository.existsById(person.getId())).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> personService.deletePerson(person));
        assertThat(thrown.getMessage()).contains(person.getId());

        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, never()).delete(any(Person.class));
    }



    // Fournit des valeurs de Firestation, y compris null
    static Stream<Person> provideInvalidPerson() {
        Person person1 = new Person(null, null, null, null, null, null, null);
        Person person2 = new Person(" ", "", "", "", null, " ", "");

        return Stream.of(person1, person2);
    }

}