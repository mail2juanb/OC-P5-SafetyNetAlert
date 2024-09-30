package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PersonsInfoLastNameServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonsInfoLastNameServiceImpl personsInfoLastNameService;


    @Test
    // On va vérifier ici que la méthode retourne une liste de PersonInfoLastName avec un lastName connu
    void getPersonsInfoLastName_shouldReturnListOfPersonInfoLastName(){
        // Given
        String lastName = "lastNameTest1";

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
        person2.setLastName("lastNameTest1");
        person2.setAddress("addressTest2");
        person2.setCity("cityTest2");
        person2.setZip(2);
        person2.setPhone("phoneTest2");
        person2.setEmail("emailTest2");

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        List<String> idsList = new ArrayList<>();
        idsList.add("firstNameTest1-lastNameTest1");
        idsList.add("firstNameTest2-lastNameTest1");

        LocalDate birthdate1 = LocalDate.parse("09/01/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = Collections.emptyList();
        List<String> allergiesList1 = Collections.emptyList();

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("firstNameTest1");
        medicalRecord1.setLastName("lastNameTest1");
        medicalRecord1.setBirthdate(birthdate1);
        medicalRecord1.setMedications(medicationList1);
        medicalRecord1.setAllergies(allergiesList1);

        LocalDate birthdate2 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList2 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("firstNameTest2");
        medicalRecord2.setLastName("lastNameTest2");
        medicalRecord2.setBirthdate(birthdate2);
        medicalRecord2.setMedications(medicationList2);
        medicalRecord2.setAllergies(allergiesList2);

        PersonWithMedicalRecord personWithMedicalRecord1 = new PersonWithMedicalRecord(person1, medicalRecord1);
        PersonWithMedicalRecord personWithMedicalRecord2 = new PersonWithMedicalRecord(person2, medicalRecord2);

        List<PersonWithMedicalRecord> personWithMedicalRecordList = new ArrayList<>();
        personWithMedicalRecordList.add(personWithMedicalRecord1);
        personWithMedicalRecordList.add(personWithMedicalRecord2);

        when(personRepository.existsByLastName(lastName)).thenReturn(true);
        when(personRepository.getAll()).thenReturn(personList);
        when(personRepository.getPersonsWithMedicalRecord(idsList)).thenReturn(personWithMedicalRecordList);

        // When
        List<PersonInfoLastName> result = personsInfoLastNameService.getPersonsInfoLastName(lastName);

        // Then
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

        // FIXME Pas certain de cet assertion. Le résultat peut changer dans le temps
        assertThat(result.get(0).getAge()).isEqualTo(4);
        assertThat(result.get(1).getAge()).isEqualTo(34);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLastName")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException lorsque le lastName est Blank
    void getPersonsInfoLastName_shouldReturnNullOrEmptyObjectExceptionWithBlankLastName(String lastName) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> personsInfoLastNameService.getPersonsInfoLastName(lastName));
        assertThat(thrown.getMessage()).satisfiesAnyOf(
                message -> assertThat(message).contains("null"),
                message -> assertThat(message).contains("empty")
        );
    }

    @Test
    // On va vérifier ici que la méthode lève une NotFoundException avec un lastName inconnu
    void getPersonsInfoLastName_shouldReturnNotFoundExceptionWithUnknownLastName() {
        // Given
        String lastName = "unknownLastName";

        when(personRepository.existsByLastName(lastName)).thenReturn(false);

        // When / Then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> personsInfoLastNameService.getPersonsInfoLastName(lastName));
        assertThat(thrown.getMessage().contains(lastName));
    }


    // Fournit des valeurs de ville, y compris null
    static Stream<String> provideInvalidLastName() {
        return Stream.of("", " ", null);
    }

}
