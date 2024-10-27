package com.oc_P5.SafetyNetAlerts.phoneAlert;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import com.oc_P5.SafetyNetAlerts.service.PhoneAlertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
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


    @Captor
    private ArgumentCaptor<List<String>> addressesArgumentCaptor;



    @Test
    void getPhonesByStation_shouldReturnListOfStringPhonesWithKnownStation() {

        // Given a Firestation List
        Firestation firestation1 = new Firestation("addressTest1", 1);

        List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(firestation1);

        List<String> addresses = new ArrayList<>();
        addresses.add(firestation1.getAddress());

        // Given a Person List
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1, "phoneTest1", "emailTest1");
        Person person2 = new Person("firstNameTest2", "lastNameTest2", "addressTest1", "cityTest1", 1, "phoneTest2", "emailTest2");
        Person person3 = new Person("firstNameTest3", "lastNameTest3", "addressTest1", "cityTest1", 1, null, null);
        Person person4 = new Person("firstNameTest4", "lastNameTest4", "addressTest1", "cityTest1", 1, null, null);
        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);

        // Given a station
        Integer stationNumber = 1;

        when(firestationRepository.getByStation(stationNumber)).thenReturn(firestationList);
        when(personRepository.getByAddresses(addresses)).thenReturn(personList);

        // When call method on service
        List<String> phones = phoneAlertService.getPhonesByStation(stationNumber);

        // Then verify that the object returned contains expected values
        assertEquals(3, phones.size());
        assertTrue(phones.contains(person1.getPhone()));
        assertTrue(phones.contains(person2.getPhone()));

        verify(firestationRepository, times(1)).getByStation(stationNumber);
        verify(personRepository, times(1)).getByAddresses(addresses);

        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationRepository).getByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(stationNumber);

        verify(personRepository).getByAddresses(addressesArgumentCaptor.capture());
        assertThat(addressesArgumentCaptor.getValue()).isEqualTo(addresses);
    }


    @Test
    void getPhonesByStation_shouldReturnEmptyListWithUnknownStation() {

        // Given an unknown firestation_number
        Integer stationNumber = 78;

        when(firestationRepository.getByStation(stationNumber)).thenReturn(Collections.emptyList());
        when(personRepository.getByAddresses(Collections.emptyList())).thenReturn(Collections.emptyList());

        // When call method on service
        List<String> phones = phoneAlertService.getPhonesByStation(stationNumber);

        // Then verify that the object returned contains expected values
        assertTrue(phones.isEmpty());

        verify(firestationRepository, times(1)).getByStation(stationNumber);
        verify(personRepository, times(1)).getByAddresses(Collections.emptyList());

        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationRepository).getByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(stationNumber);

        verify(personRepository).getByAddresses(addressesArgumentCaptor.capture());
        assertThat(addressesArgumentCaptor.getValue()).isEqualTo(Collections.emptyList());

    }


    @Test
    void getPhonesByStation_shouldReturnEmptyListWithNoPersonsCoveredByStation() {

        // Given a firestation with known station
        Firestation firestation = new Firestation("unknownAddress", 3);
        Integer stationNumber = firestation.getStation();

        // Given firestation list
        List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(firestation);

        when(firestationRepository.getByStation(stationNumber)).thenReturn(firestationList);
        when(personRepository.getByAddresses(anyList())).thenReturn(Collections.emptyList());

        // When call method on service
        List<String> phones = phoneAlertService.getPhonesByStation(stationNumber);

        // Then verify that the object returned contains expected values
        assertTrue(phones.isEmpty());
        verify(firestationRepository, times(1)).getByStation(stationNumber);
        verify(personRepository, times(1)).getByAddresses(anyList());

        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(firestationRepository).getByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(stationNumber);
    }

}