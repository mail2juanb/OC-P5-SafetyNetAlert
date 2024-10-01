package com.oc_P5.SafetyNetAlerts.service;


import com.oc_P5.SafetyNetAlerts.dto.MembersByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FloodStationsServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    FloodStationsServiceImpl floodStationsService;


    @ParameterizedTest
    @MethodSource("provideInvalidStation_Numbers")
    // On va vérifier ici que la méthode lève une NullOrEmptyObjectException lorsque la liste de station-Number est vide.
    void getMembersByStation_shouldReturnNullOrEmptyObjectExceptionWithEmptyStation(List<Integer> station_Numbers) {
        // When / Then
        NullOrEmptyObjectException thrown = assertThrows(NullOrEmptyObjectException.class, () -> floodStationsService.getMembersByStation(station_Numbers));
        assertThat(thrown.getMessage()).satisfiesAnyOf(
                message -> assertThat(message).contains("null"),
                message -> assertThat(message).contains("empty")
        );
    }

    @Test
    // On va vérifier ici que la méthode retourne la liste de MembersByStation à partir d'une liste de Station valide
    void getMembersByStation_shouldReturnMembersByStation() {
        // Given
        List<Integer> station_Numbers = new ArrayList<>();
        station_Numbers.add(1);

        List<String> addressList = new ArrayList<>();
        addressList.add("addressTest1");

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
        person1.setZip(95220);
        person1.setPhone("123-456-7890");
        person1.setEmail("emailTest1");

        List<Person> personList = new ArrayList<>();
        personList.add(person1);

        LocalDate birthdate1 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList1 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("firstNameTest1");
        medicalRecord1.setLastName("lastNameTest1");
        medicalRecord1.setBirthdate(birthdate1);
        medicalRecord1.setMedications(medicationList1);
        medicalRecord1.setAllergies(allergiesList1);

        PersonWithMedicalRecord personMedic1 = new PersonWithMedicalRecord(person1, medicalRecord1);

        List<PersonWithMedicalRecord> personWithMedicalRecordList = new ArrayList<>();
        personWithMedicalRecordList.add(personMedic1);

        List<String> idList = new ArrayList<>();
        idList.add("firstNameTest1-lastNameTest1");

        when(firestationRepository.getAll()).thenReturn(firestationList);
        when(personRepository.getByAddresses(addressList)).thenReturn(personList);
        when(personRepository.getPersonsWithMedicalRecord(idList)).thenReturn(personWithMedicalRecordList);

        // When
        MembersByStation result = floodStationsService.getMembersByStation(station_Numbers);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMemberByStationList()).hasSize(1);

        MembersByStation.MemberByStation member = result.getMemberByStationList().get(0);
        assertThat(member.getLastName()).isEqualTo(person1.getLastName());
        assertThat(member.getPhone()).isEqualTo(person1.getPhone());
        assertThat(member.getMedications()).isEqualTo(medicationList1);
        assertThat(member.getAllergies()).isEqualTo(allergiesList1);

        // FIXME Pas certain de cet assertion. Le résultat peut changer dans le temps
        assertThat(member.getAge()).isEqualTo(34);
    }


    // Fournit des valeurs de station_Numbers, y compris null
    static Stream<List<Integer>> provideInvalidStation_Numbers() {
        return Stream.of(Collections.emptyList(), Arrays.asList(1, null, 2));
    }
}
