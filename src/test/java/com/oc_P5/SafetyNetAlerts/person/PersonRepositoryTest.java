package com.oc_P5.SafetyNetAlerts.person;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepositoryImpl;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private PersonRepositoryImpl personRepository;

    private List<Person> personListMock;


    @BeforeEach
    public void setUp() {
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 1, "phoneTest1", "emailTest1");
        Person person2 = new Person("firstNameTest2", "lastNameTest2", "addressTest2", "cityTest2", 2, "phoneTest2", "emailTest2");

        personListMock = new ArrayList<>();
        personListMock.add(person1);
        personListMock.add(person2);

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setPersons(personListMock);

        when(dataReaderService.getData()).thenReturn(dataWrapperList);
    }

    @Test
    // On va vérifier ici que la méthode retourne bien les données mock
    void getAll_shouldReturnListOfPerson() {
        // When method is called
        List<Person> personList = personRepository.getAll();

        // Then return the correct list of persons
        assertEquals(2, personList.size());
        assertEquals("firstNameTest1", personList.get(0).getFirstName());
        assertEquals("firstNameTest2", personList.get(1).getFirstName());
    }

    @Test
    // On va vérifier ici que la méthode ajoute correctement une Person
    void savePerson_shouldSave() {
        // Given a new person to be added
        Person person = new Person("firstNameTest3", "lastNameTest3", "addressTest3", "cityTest3", 3, "phoneTest3", "email@Test3");

        // When the save method is called
        personRepository.save(person);

        // Then the new person added to the list
        assertEquals(3, personListMock.size());
        assertEquals("firstNameTest1", personListMock.get(0).getFirstName());
        assertEquals("firstNameTest2", personListMock.get(1).getFirstName());
        assertEquals("firstNameTest3", personListMock.get(2).getFirstName());
        assertEquals("lastNameTest3", personListMock.get(2).getLastName());
        assertEquals("addressTest3", personListMock.get(2).getAddress());
        assertEquals("cityTest3", personListMock.get(2).getCity());
        assertEquals(3, personListMock.get(2).getZip());
        assertEquals("phoneTest3", personListMock.get(2).getPhone());
        assertEquals("email@Test3", personListMock.get(2).getEmail());

    }

    @Test
    // On va vérifier ici que la méthode met à jour correctement une Person
    void updatePerson_shouldUpdate() {
        // Given a person with updated details
        Person person = new Person("firstNameTest1", "lastNameTest1", "addressTest1_Updated", "cityTest1_Updated", 10, "phoneTest1_updated", "email@Test1_updated");

        // When the update method is called
        personRepository.update(person);

        // Then person's details are updated in the list
        assertEquals(2, personListMock.size());
        assertEquals(person.getFirstName(), personListMock.getFirst().getFirstName());
        assertEquals(person.getLastName(), personListMock.getFirst().getLastName());
        assertEquals(person.getAddress(), personListMock.getFirst().getAddress());
        assertEquals(person.getCity(), personListMock.getFirst().getCity());
        assertEquals(person.getZip(), personListMock.getFirst().getZip());
        assertEquals(person.getPhone(), personListMock.getFirst().getPhone());
        assertEquals(person.getEmail(), personListMock.getFirst().getEmail());
    }

    @Test
    // On va vérifier ici que la méthode supprime correctement une Person
    void delete_shouldDelete() {
        // Given a person to be deleted
        Person person = new Person("firstNameTest1", "lastNameTest1", null, null, null, null, null);

        // When delete method is called
        personRepository.delete(person);

        // Then person is removed from the list
        assertEquals(1, personListMock.size());
        assertEquals("firstNameTest2", personListMock.getFirst().getFirstName());
        assertFalse(personListMock.contains(person));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Person par address
    void getByAddress_shouldReturnListOfPersonByAddress() {
        // Given an address to search by
        String address = "addressTest1";

        // When method is called
        List<Person> personList = personRepository.getByAddress(address);

        // Then return the persons at that address
        assertEquals(1, personList.size());
        assertEquals("addressTest1", personList.getFirst().getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Person par liste d'addresses
    void getByAddresses_shouldReturnListOfPersonByCollectionAddresses() {
        // Given a collection of addresses to search by
        Collection<String> addresses = new ArrayList<>();
        addresses.add("addressTest1");
        addresses.add("addressTest2");

        // When method is called
        List<Person> personList = personRepository.getByAddresses(addresses);

        // Then return the persons at that address
        assertEquals(2, personList.size());
        assertEquals("addressTest1", personList.get(0).getAddress());
        assertEquals("addressTest2", personList.get(1).getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Person par Ville
    void getByCity_shouldReturnListOfPersonByCity() {
        // Given a city to search by
        String city = "cityTest1";

        // When method is called
        List<Person> personList = personRepository.getByCity(city);

        // Then return the persons in that city
        assertEquals(1, personList.size());
        assertEquals("cityTest1", personList.getFirst().getCity());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour trouver une Person par id connue
    void existsById_shouldBeTrueIfIdExists() {
        // Given an existing person ID
        String id = "firstNameTest1-lastNameTest1";

        // When / Then return true when the ID exists
        assertTrue(personRepository.existsById(id));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour trouver une Person par id inconnue
    void existsById_shouldBeFalseIfIdNotExists() {
        // Given an unknown person ID
        String id = "unknownId";

        // When / Then return false when the ID not exist
        assertFalse(personRepository.existsById(id));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour trouver une Person par ville connue
    void existsByCity_shouldBeTrueIfCityExists(){
        // Given an existing city
        String city = "cityTest1";

        // When / Then return true when the city exists
        assertTrue(personRepository.existsByCity(city));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour trouver une Person par ville inconnue
    void existsByCity_shouldBeFalseIfCityNotExists(){
        // Given an unknown person ID
        String city = "unknownCityTest1";

        // When / Then return false when the city not exist
        assertFalse(personRepository.existsByCity(city));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la recherche de Person par adresse
    void findByAddress_shouldReturnPersonByAddress() {
        // Given an address to search by
        String address = "addressTest1";

        // When method is called
        Optional<Person> person = personRepository.findByAddress(address);

        // Then return the person at that address
        assertTrue(person.isPresent());
        assertEquals("addressTest1", person.get().getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour trouver une Person par lastName connu
    void existsByLastName_shouldBeTrueIfLastNameExists(){
        // Given an existing lastName
        String lastName = "lastNameTest1";

        // When / Then return true when lastName exists
        assertTrue(personRepository.existsByLastName(lastName));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour trouver une Person par ville inconnue
    void existsByLastName_shouldBeFalseIfLastNameNotExists(){
        // Given an unknown lastName
        String lastName = "unknownLastNameTest1";

        // When / Then return false when lastName not exist
        assertFalse(personRepository.existsByLastName(lastName));
    }

    @Test
    // On va vérifier que la méthode renvoi bien une liste de PersonWithMedicalRecord ainsi que les bons attributs
    void getPersonsWithMedicalRecord_shouldReturnListOfPersonWithMedicalRecord() {
        // Given a list of person IDs and corresponding mock data (persons and medical records)
        List<String> ids = new ArrayList<>();
        ids.add("firstNameTest1-lastNameTest1");
        ids.add("firstNameTest2-lastNameTest2");

        List<Person> personList = new ArrayList<>();
        personList.add(personListMock.get(0));
        personList.add(personListMock.get(1));

        LocalDate birthdate1 = LocalDate.parse("09/01/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList1 = Collections.emptyList();
        List<String> allergiesList1 = Collections.emptyList();

        MedicalRecord medicalRecord1 = new MedicalRecord("firstNameTest1", "lastNameTest1", birthdate1, medicationList1, allergiesList1);

        LocalDate birthdate2 = LocalDate.parse("09/01/1990", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<String> medicationList2 = List.of("medicationTest1:100mg", "medicationTest2:200mg");
        List<String> allergiesList2 = List.of("allergieTest1", "allergieTest2");

        MedicalRecord medicalRecord2 = new MedicalRecord("firstNameTest2", "lastNameTest2", birthdate2, medicationList2, allergiesList2);

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord1);
        medicalRecordList.add(medicalRecord2);

        DataWrapperList dataWrapper = new DataWrapperList();
        dataWrapper.setMedicalRecords(medicalRecordList);
        dataWrapper.setPersons(personList);

        when(dataReaderService.getData()).thenReturn(dataWrapper);

        // When method is called
        List<PersonWithMedicalRecord> result = personRepository.getPersonsWithMedicalRecord(ids);

        // Then return the correct persons with medical records
        assertNotNull(result);
        assertEquals(2, result.size());
            // Checking attributes for PersonWithMedicalRecord 1
        assertEquals("firstNameTest1", result.get(0).person().getFirstName());
        assertEquals("lastNameTest1", result.get(0).person().getLastName());
        assertEquals("addressTest1", result.get(0).person().getAddress());
        assertEquals("cityTest1", result.get(0).person().getCity());
        assertEquals(1, result.get(0).person().getZip());
        assertEquals("phoneTest1", result.get(0).person().getPhone());
        assertEquals("emailTest1", result.get(0).person().getEmail());
        assertEquals("firstNameTest1", result.get(0).medicalRecord().getFirstName());
        assertEquals("lastNameTest1", result.get(0).medicalRecord().getLastName());
        assertEquals(birthdate1, result.get(0).medicalRecord().getBirthdate());
        assertEquals(medicationList1, result.get(0).medicalRecord().getMedications());
        assertEquals(allergiesList1, result.get(0).medicalRecord().getAllergies());
            // Checking attributes for PersonWithMedicalRecord 2
        assertEquals("firstNameTest2", result.get(1).person().getFirstName());
        assertEquals("lastNameTest2", result.get(1).person().getLastName());
        assertEquals("addressTest2", result.get(1).person().getAddress());
        assertEquals("cityTest2", result.get(1).person().getCity());
        assertEquals(2, result.get(1).person().getZip());
        assertEquals("phoneTest2", result.get(1).person().getPhone());
        assertEquals("emailTest2", result.get(1).person().getEmail());
        assertEquals("firstNameTest2", result.get(1).medicalRecord().getFirstName());
        assertEquals("lastNameTest2", result.get(1).medicalRecord().getLastName());
        assertEquals(birthdate2, result.get(1).medicalRecord().getBirthdate());
        assertEquals(medicationList2, result.get(1).medicalRecord().getMedications());
        assertEquals(allergiesList2, result.get(1).medicalRecord().getAllergies());
    }

}