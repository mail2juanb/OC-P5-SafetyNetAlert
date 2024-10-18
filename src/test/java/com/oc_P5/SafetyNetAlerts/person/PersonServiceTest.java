package com.oc_P5.SafetyNetAlerts.person;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepositoryImpl;
import com.oc_P5.SafetyNetAlerts.service.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    // On va vérifier ici que lorsqu'une Person est ajoutée, elle est correctement sauvegardée avec les bons attributs.
    void addPerson_ShouldAddPersonWhenNotExist() {
        // Given an unknown Person to add
        Person person = new Person("addFirstName", "addLastName", "addAddress", "addCity", 9999, "123-456-9999", "addEmail");

        when(personRepository.existsById(person.getId())).thenReturn(false);

        // When call method on service
        personService.addPerson(person);

        // Then verify that the object sent is correctly distributed
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(person);

        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, times(1)).save(person);
    }

    @Test
    // On va vérifier ici que la méthode lève une ConflictException lorsque la Person existe déjà
    void addPerson_shouldReturnConflictExceptionWhenExists() {
        // Given a known Person to add
        Person person = personListMock.getFirst();

        when(personRepository.existsById(person.getId())).thenReturn(true);

        // When / Then a NotFoundException is thrown
        ConflictException thrown = assertThrows(ConflictException.class, () -> personService.addPerson(person));
        assertThat(thrown.getMessage()).contains(person.getFirstName());
        assertThat(thrown.getMessage()).contains(person.getLastName());

        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, never()).save(person);
    }

    @Test
    // On va vérifier ici que la Person est mise à jour avec une Person valide
    void updatePerson_shouldUpdatePersonWhenExists() {
        // Given a known Person to update
        Person person = new Person("firstNameTest1", "lastNameTest1", "newAddressTest1", "newCityTest1", 1111, "123-456-7891", "emailTest1");

        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        // When call method on service
        personService.updatePerson(person);

        // Then verify that the object sent is correctly distributed
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

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque la Person n'existe pas
    void updatePerson_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given an unknown Person to update
        Person person = new Person("notExistFirstName", "notExistLastName", "notExistAddress", "notExistCity", 1111, "123-456-7891", "emailTest1");

        when(personRepository.findById(person.getId())).thenReturn(Optional.empty());

        // When / Then a NotFoundException is thrown
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> personService.updatePerson(person));
        assertThat(thrown.getMessage()).contains(person.getId());

        verify(personRepository, times(1)).findById(person.getId());
        verify(personRepository, never()).update(any(Person.class));
    }

    @Test
    // On va vérifier ici que le MedicalRecord est supprimé avec une Person valide
    void deleteMedicalRecord_shouldDeleteMedicalRecord() {
        // Given a known Person to delete
        Person person = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7891", "emailTest1");

        when(personRepository.existsById(person.getId())).thenReturn(true);

        // When call method on service
        personService.deletePerson(person);

        // Then verify that the object sent is correctly distributed
        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, times(1)).delete(person);

        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).delete(personArgumentCaptor.capture());
        Person deletedPerson = personArgumentCaptor.getValue();
        assertThat(deletedPerson).isEqualTo(person);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque la Person n'existe pas
    void deletePerson_shouldReturnNotFoundExceptionWhenNotExist() {
        // Given an unknown Person to delete
        Person person = new Person("unknownFirstName", "unknownLastName", "unknownAddress", "cityTest1", 1111, "123-456-7891", "emailTest1");

        when(personRepository.existsById(person.getId())).thenReturn(false);

        // When / Then a NotFoundException is thrown
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> personService.deletePerson(person));
        assertThat(thrown.getMessage()).contains(person.getId());

        verify(personRepository, times(1)).existsById(person.getId());
        verify(personRepository, never()).delete(any(Person.class));
    }

}