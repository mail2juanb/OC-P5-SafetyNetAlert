package com.oc_P5.SafetyNetAlerts.childAlert;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import com.oc_P5.SafetyNetAlerts.service.ChildAlertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChildAlertServiceTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    ChildAlertServiceImpl childAlertService;



    @Test
    // On va vérifier ici que la méthode retourne bien une liste de ChildrenByAddress ainsi que les attributs nécessaires avec une address valide.
    void getChildByAddress_shouldReturnListOfChildrenByAddress() {
        // Given a known address
        String address = "addressTest1";

        // Person Test data creation
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1111, "123-456-7890", "emailTest1");
        Person person3 = new Person("firstNameTest3", "lastNameTest1", "addressTest1", "cityTest1", 1111, "323-456-7890", "emailTest3");

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person3);

        List<String> idList = new ArrayList<>();
        idList.add("firstNameTest1-lastNameTest1");
        idList.add("firstNameTest3-lastNameTest1");

        // MedicalRecord Test data creation
        LocalDate birthdate1 = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = Collections.emptyList();
        List<String> allergiesList1 = Collections.emptyList();
        MedicalRecord medicalRecord1 = new MedicalRecord("firstNameTest1", "lastNameTest1", birthdate1, medicationList1, allergiesList1);

        LocalDate birthdate3 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList3 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList3 = List.of("allergieTest1", "allergieTest2");
        MedicalRecord medicalRecord3 = new MedicalRecord("firstNameTest3", "lastNameTest1", birthdate3, medicationList3, allergiesList3);

        // PersonWithMedicalRecord Test data creation
        List<PersonWithMedicalRecord> personWithMedicalRecordList = new ArrayList<>();
        personWithMedicalRecordList.add(new PersonWithMedicalRecord(person1, medicalRecord1));
        personWithMedicalRecordList.add(new PersonWithMedicalRecord(person3, medicalRecord3));

        when(personRepository.getByAddress(address)).thenReturn(personList);
        when(personRepository.getPersonsWithMedicalRecord(idList)).thenReturn(personWithMedicalRecordList);

        // When call service method
        List<ChildrenByAddress> result = childAlertService.getChildByAddress(address);

        // Then verify that the object returned contains expected values
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getFirstName()).isEqualTo(person1.getFirstName());
        assertThat(result.getFirst().getLastName()).isEqualTo(person1.getLastName());
        assertThat(result.getFirst().getFamilyMembers().getFirst().getFirstName()).isEqualTo(person3.getFirstName());
        assertThat(result.getFirst().getFamilyMembers().getFirst().getLastName()).isEqualTo(person3.getLastName());

        int expectedAge = Period.between(birthdate1, LocalDate.now()).getYears();
        assertThat(result.getFirst().getAge()).isEqualTo(expectedAge);

        verify(personRepository, times(1)).getByAddress(address);
        verify(personRepository, times(1)).getPersonsWithMedicalRecord(idList);

        ArgumentCaptor<String> addressArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(personRepository).getByAddress(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);
    }

    @Test
    // On va vérifier ici que la méthode retourne bien une liste vide avec une address non existante.
    void getChildByAddress_shouldReturnEmptyListWhenAddressNotFound() {
        // Given an unknown address
        String address = "unknownAddress";

        // When call service method
        List<ChildrenByAddress> result = childAlertService.getChildByAddress(address);

        // Then verify that the object returned is empty
        assertThat(result).isEmpty();

        verify(personRepository, times(1)).getByAddress(anyString());
        verify(personRepository, times(1)).getPersonsWithMedicalRecord(anyList());
    }

}