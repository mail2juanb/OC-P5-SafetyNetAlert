package com.oc_P5.SafetyNetAlerts.service;


import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommunityEmailServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CommunityEmailServiceImpl communityEmailService;


    @Test
    // On va vérifier ici que la méthode renvoi bien la liste des emails par ville avec une ville connue
    void getCommunityEmailByCity_shouldReturnListOfStringEmail(){
        // Given
        String city = "cityTest1";

        Person person1 = new Person();
        person1.setFirstName("firstNameTest1");
        person1.setLastName("lastNameTest1");
        person1.setAddress("addressTest1");
        person1.setCity("cityTest1");
        person1.setZip(1);
        person1.setPhone("phoneTest1");
        person1.setEmail("emailTest1");

        Person person2 = new Person();
        person2.setFirstName("firstNameTest2");
        person2.setLastName("lastNameTest2");
        person2.setAddress("addressTest2");
        person2.setCity("cityTest1");
        person2.setZip(1);
        person2.setPhone("phoneTest2");
        person2.setEmail("emailTest2");

        Person person3 = new Person();
        person3.setFirstName("firstNameTest3");
        person3.setLastName("lastNameTest3");
        person3.setAddress("addressTest1");
        person3.setCity("cityTest1");
        person3.setZip(1);
        person3.setPhone(null);
        person3.setEmail(null);

        Person person4 = new Person();
        person4.setFirstName("firstNameTest4");
        person4.setLastName("lastNameTest4");
        person4.setAddress("addressTest1");
        person4.setCity("cityTest1");
        person4.setZip(1);
        person4.setPhone("");
        person4.setEmail("");

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);

        when(personRepository.existsByCity(city)).thenReturn(true);
        when(personRepository.getByCity(city)).thenReturn(personList);

        // When
        List<String> emails = communityEmailService.getCommunityEmailByCity(city);

        // Then
        assertEquals(2, emails.size());
        assertTrue(emails.contains(person1.getEmail()));
        assertTrue(emails.contains(person2.getEmail()));

    }

    @ParameterizedTest
    @MethodSource("provideInvalidCities")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException avec une ville Blank
    void getCommunityEmailByCity_shouldReturnNullOrEmptyObjectExceptionWithBlankCity(String city){
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> communityEmailService.getCommunityEmailByCity(city));
        assertThat(thrown.getMessage()).satisfiesAnyOf(
                message -> assertThat(message).contains("null"),
                message -> assertThat(message).contains("empty")
        );
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException avec une ville inconnue
    void getCommunityEmailByCity_shouldReturnNotFoundExceptionWithUnknownCity() {
        // Given
        String city = "unknownCityTest";

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> communityEmailService.getCommunityEmailByCity(city));
        assertThat(thrown.getMessage().contains(city));
    }


    // Fournit des valeurs de ville, y compris null
    static Stream<String> provideInvalidCities() {
        return Stream.of("", " ", null);
    }

}
