package com.oc_P5.SafetyNetAlerts.floodStations;

import com.oc_P5.SafetyNetAlerts.dto.MemberByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import com.oc_P5.SafetyNetAlerts.service.FloodStationsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FloodStationsServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private FloodStationsServiceImpl floodStationsService;


    @Captor
    private ArgumentCaptor<List<String>> addressListArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<String>> idListArgumentCaptor;



    @Test
    // On va vérifier ici que la méthode retourne la liste de MembersByStation à partir d'une liste de Station valide
    void getMembersByStation_shouldReturnMembersByStation() {
        // Given a list of station_number
        List<Integer> station_Numbers = new ArrayList<>();
        station_Numbers.add(1);

        // Given the address list corresponding
        List<String> addressList = new ArrayList<>();
        addressList.add("addressTest1");

        // Given a list of Firestation
        Firestation firestation1 = new Firestation("addressTest1", 1);
        List<Firestation> firestationList = new ArrayList<>();
        firestationList.add(firestation1);

        // Given a list of Person
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 95220, "123-456-7890", "emailTest1");
        List<Person> personList = new ArrayList<>();
        personList.add(person1);

        // Given a MedicalRecord
        LocalDate birthdate1 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList1 = List.of("allergieTest1", "allergieTest2");
        MedicalRecord medicalRecord1 = new MedicalRecord("firstNameTest1", "lastNameTest1", birthdate1, medicationList1, allergiesList1);

        // Given list of PersonWithMedicalRecord
        PersonWithMedicalRecord personMedic1 = new PersonWithMedicalRecord(person1, medicalRecord1);
        List<PersonWithMedicalRecord> personWithMedicalRecordList = new ArrayList<>();
        personWithMedicalRecordList.add(personMedic1);

        // Given an id list
        List<String> idList = new ArrayList<>();
        idList.add("firstNameTest1-lastNameTest1");


        when(firestationRepository.getAll()).thenReturn(firestationList);
        when(personRepository.getByAddresses(addressList)).thenReturn(personList);
        when(personRepository.getPersonsWithMedicalRecord(idList)).thenReturn(personWithMedicalRecordList);

        // When call method on service
        List<MemberByStation> result = floodStationsService.getMembersByStation(station_Numbers);

        // Then verify that the object returned contains expected values
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getLastName()).isEqualTo(person1.getLastName());
        assertThat(result.getFirst().getPhone()).isEqualTo(person1.getPhone());
        assertThat(result.getFirst().getMedications()).isEqualTo(medicalRecord1.getMedications());
        assertThat(result.getFirst().getAllergies()).isEqualTo(medicalRecord1.getAllergies());
        int expectedAge = Period.between(birthdate1, LocalDate.now()).getYears();
        assertThat(result.getFirst().getAge()).isEqualTo(expectedAge);

        verify(firestationRepository, times(1)).getAll();
        verify(personRepository, times(1)).getByAddresses(addressList);
        verify(personRepository, times(1)).getPersonsWithMedicalRecord(idList);

        verify(personRepository).getByAddresses(addressListArgumentCaptor.capture());
        assertThat(addressListArgumentCaptor.getValue()).isEqualTo(addressList);

        verify(personRepository).getPersonsWithMedicalRecord(idListArgumentCaptor.capture());
        assertThat(idListArgumentCaptor.getValue()).isEqualTo(idList);

    }

}