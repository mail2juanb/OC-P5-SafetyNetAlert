package com.oc_P5.SafetyNetAlerts.personsInfoLastName;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import com.oc_P5.SafetyNetAlerts.service.PersonsInfoLastNameServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PersonsInfoLastNameServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonsInfoLastNameServiceImpl personsInfoLastNameService;


    @Captor
    ArgumentCaptor<List<String>> idsArgumentCaptor;



    @Test
    // On va vérifier ici que la méthode retourne une liste de PersonInfoLastName avec un lastName connu
    void getPersonsInfoLastName_shouldReturnListOfPersonInfoLastName(){
        // Given a known lastName
        String lastName = "lastNameTest1";

        // Given a list of Person
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1, "phoneTest1", "emailTest1");
        Person person2 = new Person("firstNameTest2", "lastNameTest1", "addressTest2", "cityTest2", 2, "phoneTest2", "emailTest2");
        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        // Given a list of ids
        List<String> idsList = new ArrayList<>();
        idsList.add("firstNameTest1-lastNameTest1");
        idsList.add("firstNameTest2-lastNameTest1");

        // Given a list of PersonWithMedicalRecord
        LocalDate birthdate1 = LocalDate.parse("09/01/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = Collections.emptyList();
        List<String> allergiesList1 = Collections.emptyList();
        LocalDate birthdate2 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList2 = List.of("allergieTest1", "allergieTest2");
        MedicalRecord medicalRecord1 = new MedicalRecord("firstNameTest1", "lastNameTest1", birthdate1, medicationList1, allergiesList1);
        MedicalRecord medicalRecord2 = new MedicalRecord("firstNameTest2", "lastNameTest2", birthdate2, medicationList2, allergiesList2);
        PersonWithMedicalRecord personWithMedicalRecord1 = new PersonWithMedicalRecord(person1, medicalRecord1);
        PersonWithMedicalRecord personWithMedicalRecord2 = new PersonWithMedicalRecord(person2, medicalRecord2);
        List<PersonWithMedicalRecord> personWithMedicalRecordList = new ArrayList<>();
        personWithMedicalRecordList.add(personWithMedicalRecord1);
        personWithMedicalRecordList.add(personWithMedicalRecord2);

        when(personRepository.existsByLastName(lastName)).thenReturn(true);
        when(personRepository.getAll()).thenReturn(personList);
        when(personRepository.getPersonsWithMedicalRecord(idsList)).thenReturn(personWithMedicalRecordList);

        // When call method on service
        List<PersonInfoLastName> result = personsInfoLastNameService.getPersonsInfoLastName(lastName);

        // Then verify that the object returned contains expected values
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getLastName()).isEqualTo("lastNameTest1");
        assertThat(result.get(1).getLastName()).isEqualTo("lastNameTest1");
        assertThat(result.get(0).getAddress()).isEqualTo("addressTest1");
        assertThat(result.get(1).getAddress()).isEqualTo("addressTest2");
        assertThat(result.get(0).getPhone()).isEqualTo("phoneTest1");
        assertThat(result.get(1).getPhone()).isEqualTo("phoneTest2");
        assertThat(result.get(0).getMedications()).isEqualTo(medicalRecord1.getMedications());
        assertThat(result.get(1).getMedications()).isEqualTo(medicalRecord2.getMedications());
        assertThat(result.get(0).getAllergies()).isEqualTo(medicalRecord1.getAllergies());
        assertThat(result.get(1).getAllergies()).isEqualTo(medicalRecord2.getAllergies());

        int expectedAge1 = Period.between(birthdate1, LocalDate.now()).getYears();
        int expectedAge2 = Period.between(birthdate2, LocalDate.now()).getYears();
        assertThat(result.get(0).getAge()).isEqualTo(expectedAge1);
        assertThat(result.get(1).getAge()).isEqualTo(expectedAge2);

        // Then capturing the lastName passed to the repository
        ArgumentCaptor<String> lastNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(personRepository).existsByLastName(lastNameArgumentCaptor.capture());
        assertThat(lastNameArgumentCaptor.getValue()).isEqualTo(lastName);

        verify(personRepository).getPersonsWithMedicalRecord(idsArgumentCaptor.capture());
        assertThat(idsArgumentCaptor.getValue()).isEqualTo(idsList);
    }


    @Test
    // On va vérifier ici que la méthode lève une NotFoundException avec un lastName inconnu
    void getPersonsInfoLastName_shouldReturnNotFoundExceptionWithUnknownLastName() {
        // Given an unknown lastName
        String lastName = "unknownLastName";

        when(personRepository.existsByLastName(lastName)).thenReturn(false);

        // When / Then method throws a NotFoundException with a message containing the lastName
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> personsInfoLastNameService.getPersonsInfoLastName(lastName));
        assertThat(thrown.getMessage()).contains(lastName);

        verify(personRepository, times(1)).existsByLastName(anyString());
        verify(personRepository, never()).getAll();
        verify(personRepository, never()).getPersonsWithMedicalRecord(anyList());
    }

}