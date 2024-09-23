package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        // Initialisation des mocks fait par l'annotation @ExtendWith(MockitoExtension.class)
        // Creation des données de test - String *firstName*, String *lastName*, String address, String city, Integer zip, String phone, String email
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
        person2.setAddress("addressTest2");
        person2.setCity("cityTest2");
        person2.setZip(2);
        person2.setPhone("phoneTest2");
        person2.setEmail("emailTest2");

        personListMock = new ArrayList<>();
        personListMock.add(person1);
        personListMock.add(person2);

        DataWrapperList dataWrapperList = new DataWrapperList();
        dataWrapperList.setPersons(personListMock);

        // Configure le mock pour PersonRepository
        when(dataReaderService.getData()).thenReturn(dataWrapperList);
    }

    @Test
    // On va vérifier ici que la méthode retourne bien les données mock
    void getAll_shouldReturnListOfPersons() {
        // When
        List<Person> personList = personRepository.getAll();

        // Then
        assertEquals(2, personList.size());
        assertEquals("firstNameTest1", personList.get(0).getFirstName());
        assertEquals("firstNameTest2", personList.get(1).getFirstName());
    }

    @Test
    // On va vérifier ici que la méthode ajoute correctement une Person
    void savePerson_shouldSave() {
        // Given
        Person person = new Person();
        person.setFirstName("firstNameTest3");
        person.setLastName("lastNameTest3");
        person.setAddress("adressTest3");
        person.setCity("cityTest3");
        person.setZip(3);
        person.setPhone("phoneTest3");
        person.setEmail("emailTest3");

        // When
        personRepository.save(person);

        // Then
        assertEquals(3, personListMock.size());
        assertEquals("firstNameTest1", personListMock.get(0).getFirstName());
        assertEquals("firstNameTest2", personListMock.get(1).getFirstName());
        assertEquals("firstNameTest3", personListMock.get(2).getFirstName());
        assertEquals("lastNameTest3", personListMock.get(2).getLastName());
        assertEquals("adressTest3", personListMock.get(2).getAddress());
        assertEquals("cityTest3", personListMock.get(2).getCity());
        assertEquals(3, personListMock.get(2).getZip());
        assertEquals("phoneTest3", personListMock.get(2).getPhone());
        assertEquals("emailTest3", personListMock.get(2).getEmail());

    }

    @Test
    // On va vérifier ici que la méthode met à jour correctement une Person
    void updatePerson_shouldUpdate() {
        // Given
        Person person = new Person();
        person.setFirstName("firstNameTest1");
        person.setLastName("lastNameTest1");
        person.setAddress("addressTest1_Updated");
        person.setCity("cityTest1_Updated");
        person.setZip(10);
        person.setPhone("phoneTest1_updated");
        person.setEmail("emailTest1_updated");

        // When
        personRepository.update(person);

        // Then
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
        // Given
        Person person = new Person();
        person.setFirstName("firstNameTest1");
        person.setLastName("lastNameTest1");
        person.setAddress(null);
        person.setCity(null);
        person.setZip(null);
        person.setPhone(null);
        person.setEmail(null);

        // When
        personRepository.delete(person);

        // Then
        assertEquals(1, personListMock.size());
        assertEquals("firstNameTest2", personListMock.getFirst().getFirstName());
        assertFalse(personListMock.contains(person));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Person par address
    void getByAddress_shouldReturnListOfPersonByAddress() {
        // Given
        String address = "addressTest1";

        // When
        List<Person> personList = personRepository.getByAddress(address);

        // Then
        assertEquals(1, personList.size());
        assertEquals("addressTest1", personList.getFirst().getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Person par liste d'addresses
    void getByAddresses_shouldReturnListOfPersonByCollectionAddresses() {
        // Given
        Collection<String> addresses = new ArrayList<>();
        addresses.add("addressTest1");
        addresses.add("addressTest2");

        // When
        List<Person> personList = personRepository.getByAddresses(addresses);

        // Then
        assertEquals(2, personList.size());
        assertEquals("addressTest1", personList.get(0).getAddress());
        assertEquals("addressTest2", personList.get(1).getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la récupération de la liste des Person par Ville
    void getByCity_shouldReturnListOfPersonByCity() {
        // Given
        String city = "cityTest1";

        // When
        List<Person> personList = personRepository.getByCity(city);

        // Then
        assertEquals(1, personList.size());
        assertEquals("cityTest1", personList.getFirst().getCity());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une trouver une Person par id connue
    void existsById_shouldBeTrueIfIdExists() {
        // Given
        String id = "firstNameTest1-lastNameTest1";

        // When / Then
        assertTrue(personRepository.existsById(id));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une trouver une Person par id inconnue
    void existsById_shouldBeFalseIfIdNotExists() {
        // Given
        String id = "unknownId";

        // When / Then
        assertFalse(personRepository.existsById(id));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une trouver une Person par ville connue
    void existsByCity_shouldBeTrueIfCityExists(){
        // Given
        String city = "cityTest1";

        // When / Then
        assertTrue(personRepository.existsByCity(city));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une trouver une Person par ville inconnue
    void existsByCity_shouldBeFalseIfCityNotExists(){
        // Given
        String city = "unknownCityTest1";

        // When / Then
        assertFalse(personRepository.existsByCity(city));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de de la recherche de Person par adresse
    void findByAddress_shouldReturnPersonByAddress() {
        // Given
        String address = "addressTest1";

        // When
        Optional<Person> person = personRepository.findByAddress(address);

        // Then
        assertTrue(person.isPresent());
        assertEquals("addressTest1", person.get().getAddress());
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une trouver une Person par ville connue
    void existsByLastName_shouldBeTrueIfLastNameExists(){
        // Given
        String lastName = "lastNameTest1";

        // When / Then
        assertTrue(personRepository.existsByLastName(lastName));
    }

    @Test
    // On va vérifier ici le bon fonctionnement de la réponse pour une trouver une Person par ville inconnue
    void existsByLastName_shouldBeFalseIfLastNameNotExists(){
        // Given
        String lastName = "unknownLastNameTest1";

        // When / Then
        assertFalse(personRepository.existsByLastName(lastName));
    }

}
