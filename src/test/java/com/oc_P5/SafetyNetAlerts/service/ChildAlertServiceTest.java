package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChildAlertServiceTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    ChildAlertServiceImpl childAlertService;


    @ParameterizedTest
    @MethodSource("provideInvalidAddress")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException lorsque address est vide ou null.
    void getChildByAddress_shouldReturnNullOrEmptyObjectExceptionWithAddressEmpty(String address) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> childAlertService.getChildByAddress(address));
        assertThat(thrown.getMessage()).satisfies(message -> assertThat(message).containsAnyOf("null", "empty"));
    }

    @Test
    // On va vérifier ici que la méthode retourne bien une liste de ChildrenByAddress ainsi que les attributs nécessaires avec une address valide.
    void getChildByAddress_shouldReturnListOfChildrenByAddress() {
        // Given
        String address = "addressTest1";

        Person person1 = new Person();
        person1.setFirstName("firstNameTest1");
        person1.setLastName("lastNameTest1");
        person1.setAddress("addressTest1");
        person1.setCity("cityTest1");
        person1.setZip(1111);
        person1.setPhone("123-456-7890");
        person1.setEmail("emailTest1");

        Person person2 = new Person();
        person2.setFirstName("firstNameTest2");
        person2.setLastName("lastNameTest2");
        person2.setAddress("addressTest2");
        person2.setCity("cityTest2");
        person2.setZip(2222);
        person2.setPhone("223-456-7890");
        person2.setEmail("emailTest2");

        Person person3 = new Person();
        person3.setFirstName("firstNameTest3");
        person3.setLastName("lastNameTest1");
        person3.setAddress("addressTest1");
        person3.setCity("cityTest1");
        person3.setZip(1111);
        person3.setPhone("333-456-7890");
        person3.setEmail("emailTest3");

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person3);

        List<String> idList = new ArrayList<>();
        idList.add("firstNameTest1-lastNameTest1");
        idList.add("firstNameTest3-lastNameTest1");

        LocalDate birthdate1 = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = Collections.emptyList();
        List<String> allergiesList1 = Collections.emptyList();

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("firstNameTest1");
        medicalRecord1.setLastName("lastNameTest1");
        medicalRecord1.setBirthdate(birthdate1);
        medicalRecord1.setMedications(medicationList1);
        medicalRecord1.setAllergies(allergiesList1);

        LocalDate birthdate2 = LocalDate.parse("12/05/1992", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = List.of("medicationTest1:200mg");
        List<String> allergiesList2 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("firstNameTest2");
        medicalRecord2.setLastName("lastNameTest2");
        medicalRecord2.setBirthdate(birthdate2);
        medicalRecord2.setMedications(medicationList2);
        medicalRecord2.setAllergies(allergiesList2);

        LocalDate birthdate3 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList3 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList3 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecord3 = new MedicalRecord();
        medicalRecord3.setFirstName("firstNameTest3");
        medicalRecord3.setLastName("lastNameTest1");
        medicalRecord3.setBirthdate(birthdate3);
        medicalRecord3.setMedications(medicationList3);
        medicalRecord3.setAllergies(allergiesList3);

        List<PersonWithMedicalRecord> personWithMedicalRecordList = new ArrayList<>();
        personWithMedicalRecordList.add(new PersonWithMedicalRecord(person1, medicalRecord1));
        personWithMedicalRecordList.add(new PersonWithMedicalRecord(person3, medicalRecord3));

        when(personRepository.getByAddress(address)).thenReturn(personList);
        when(personRepository.getPersonsWithMedicalRecord(idList)).thenReturn(personWithMedicalRecordList);

        // When
        List<ChildrenByAddress> result = childAlertService.getChildByAddress(address);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getFirstName()).isEqualTo(person1.getFirstName());
        assertThat(result.getFirst().getLastName()).isEqualTo(person1.getLastName());
        assertThat(result.getFirst().getFamilyMembers().getFirst().getFirstName()).isEqualTo(person3.getFirstName());
        assertThat(result.getFirst().getFamilyMembers().getFirst().getLastName()).isEqualTo(person3.getLastName());

        int expectedAge = Period.between(birthdate1, LocalDate.now()).getYears();
        assertThat(result.getFirst().getAge()).isEqualTo(expectedAge);

        verify(personRepository, times(1)).getByAddress(address);
        verify(personRepository, times(1)).getPersonsWithMedicalRecord(idList);
    }

    @Test
    // On va vérifier ici que la méthode retourne bien une liste vide avec une address non existante.
    void getChildByAddress_shouldReturnEmptyListWhenAddressNotFound() {
        // Given
        String address = "unknownAddress";

        // When
        List<ChildrenByAddress> result = childAlertService.getChildByAddress(address);

        // Then
        assertThat(result).isEmpty();
    }


    // Fournit des valeurs de address, y compris null
    static Stream<String> provideInvalidAddress() {
        return Stream.of(null, "", " ");
    }

}
