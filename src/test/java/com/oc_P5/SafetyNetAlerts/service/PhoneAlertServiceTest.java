package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhoneAlertServiceTest {

    @Mock
    FirestationRepository firestationRepository;

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    PhoneAlertServiceImpl phoneAlertService;

    @Test
    // On va vérifier ici que la méthode renvoi une liste de phones avec une station valide
    void getPhonesByStation_shouldReturnListOfStringPhonesWithKnownStation() {
        // Given
        Integer firestation_Number = 1;

        Firestation firestation1 = new Firestation();
        firestation1.setAddress("addressTest1");
        firestation1.setStation(1);

        List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(firestation1);

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
        person2.setAddress("addressTest1");
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

        List<String> addresses = new ArrayList<>();
        addresses.add(firestation1.getAddress());

        when(firestationRepository.existsByStation(firestation_Number)).thenReturn(true);
        when(firestationRepository.getByStation(firestation_Number)).thenReturn(firestationList);
        when(personRepository.getByAddresses(addresses)).thenReturn(personList);

        // When
        List<String> phones = phoneAlertService.getPhonesByStation(firestation_Number);

        // Then
        assertEquals(2, phones.size());
        assertTrue(phones.contains(person1.getPhone()));
        assertTrue(phones.contains(person2.getPhone()));

        verify(firestationRepository, times(1)).existsByStation(firestation_Number);
        verify(firestationRepository, times(1)).getByStation(firestation_Number);
        verify(personRepository, times(1)).getByAddresses(addresses);
    }

    @Test
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException lorsque le stationNumber est null
    void getPhonesByStation_shouldReturnNullOrEmptyObjectExceptionWithNullStation() {
        // Given
        Integer firestation_Number = null;

        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> phoneAlertService.getPhonesByStation(firestation_Number));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException lorsque le stationNumber est inconnue
    void getPhonesByStation_shouldReturnNotFoundExceptionWithUnknownStation() {
        // Given
        Integer firestation_Number = 99;

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> phoneAlertService.getPhonesByStation(firestation_Number));
        assertThat(thrown.getMessage()).contains(firestation_Number.toString());
    }

}
