package com.oc_P5.SafetyNetAlerts.communityEmail;


import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import com.oc_P5.SafetyNetAlerts.service.CommunityEmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommunityEmailServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CommunityEmailServiceImpl communityEmailService;


    @Test
    // On va vérifier ici que la méthode renvoi bien la liste des emails par ville avec une ville connue
    void getCommunityEmailByCity_shouldReturnListOfStringEmail(){
        // Given a valid city and persons living in that city with respective emails
        String city = "cityTest1";

        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1, "phoneTest1", "emailTest1");
        Person person2 = new Person("firstNameTest2", "lastNameTest2", "addressTest2", "cityTest1", 1, "phoneTest2", "emailTest2");

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        when(personRepository.existsByCity(city)).thenReturn(true);
        when(personRepository.getByCity(city)).thenReturn(personList);

        // When method is called
        List<String> emails = communityEmailService.getCommunityEmailByCity(city);

        // Then method should return a list of emails
        assertEquals(2, emails.size());
        assertTrue(emails.contains(person1.getEmail()));
        assertTrue(emails.contains(person2.getEmail()));

        // Then repository methods should be called once with the correct arguments
        verify(personRepository, times(1)).existsByCity(city);
        verify(personRepository, times(1)).getByCity(city);

        // Then capturing the city passed to the repository
        ArgumentCaptor<String> cityArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(personRepository).getByCity(cityArgumentCaptor.capture());
        String capturedCity = cityArgumentCaptor.getValue();
        assertThat(capturedCity).isEqualTo(city);
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException avec une ville inconnue
    void getCommunityEmailByCity_shouldReturnNotFoundExceptionWithUnknownCity() {
        // Given an unknown city
        String city = "unknownCityTest";

        // When / Then a NotFoundException is thrown and the message contains the city name
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> communityEmailService.getCommunityEmailByCity(city));
        assertThat(thrown.getMessage()).contains(city);

        verify(personRepository, times(1)).existsByCity(city);
        verify(personRepository, never()).getByCity(city);
    }

}