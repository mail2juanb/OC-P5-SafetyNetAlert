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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryImplTest {

    @Mock
    private DataReader dataReaderService;

    @InjectMocks
    private PersonRepositoryImpl personRepository;

    private List<Person> personListMock;
    private DataWrapperList dataWrapperListMock;


    @BeforeEach
    public void setUp() {
        // Initialisation des mocks fait par l'annotation @ExtendWith(MockitoExtension.class)
        // Creation des données de test - *firstName*, *lastName*, address, city, zip, phone, email
        Person personTest1 = new Person();
        personTest1.setFirstName("firstNameTest1");
        personTest1.setLastName("lastNameTest1");
        personTest1.setAddress("addressTest1");
        personTest1.setCity("cityTest1");
        personTest1.setZip(1);
        personTest1.setPhone("phoneTest1");
        personTest1.setEmail("emailTest1");

        Person personTest2 = new Person();
        personTest2.setFirstName("firstNameTest2");
        personTest2.setLastName("lastNameTest2");
        personTest2.setAddress("addressTest2");
        personTest2.setCity("cityTest2");
        personTest2.setZip(2);
        personTest2.setPhone("phoneTest2");
        personTest2.setEmail("emailTest2");

        personListMock = new ArrayList<>(Arrays.asList(personTest1, personTest2));

        // Création et configuration du mock pour DataWrapperList
        dataWrapperListMock = mock(DataWrapperList.class);
        when(dataWrapperListMock.getPersons()).thenReturn(personListMock);

        // Création et configuration du mock pour DataReader
        when(dataReaderService.getData()).thenReturn(dataWrapperListMock);

    }

    @Test
    // On va vérifier ici que la méthode retourne bien les données mock
    void getPersons_shouldReturnListOfPersons() {
        // When
        List<Person> persons = personRepository.getPersons();

        // Then
        assertEquals(2, persons.size());
        assertEquals("firstNameTest1", persons.get(0).getFirstName());
        assertEquals("firstNameTest2", persons.get(1).getFirstName());
    }


    @Test
    // On va vérifier ici que la méthode renvoi bien une liste de Person ayant l'adresse demandée (String)
    void getPersonsByAddress_shouldReturnListOfPerson() {
        // Given
        String personAdress1 = "addressTest1";

        // When
        List<Person> persons = personRepository.getPersonsByAddresses(personAdress1);

        // Then
        assertEquals(1, persons.size());
        assertEquals("firstNameTest1", persons.getFirst().getFirstName());
    }


    @Test
    // On va vérifier ici que la méthode renvoi bien une liste de Person ayant les adresses demandées (Collection<String>)
    void getPersonsByAddresses_shouldReturnListOfPerson() {
        // Given
        List<Person> persons = personRepository.getPersons();
        Collection<String> personAddresses = persons
                .stream()
                .map(Person::getAddress)
                .collect(Collectors.toSet());
        Set<String> expectedFirstNames = Set.of("firstNameTest1", "firstNameTest2");

        // When
        List<Person> personsByAddress = personRepository.getPersonsByAddresses(personAddresses);

        // Then
        String firstName0 = personsByAddress.get(0).getFirstName();
        String firstName1 = personsByAddress.get(1).getFirstName();
        assertTrue(expectedFirstNames.contains(firstName0) && expectedFirstNames.contains(firstName1));
        assertEquals(2, personsByAddress.size());
    }

    @Test
    // On va vérifier ici que la méthode renvoi bien le bon Optional<Person> avec l'id demandé
    void findPersonById_shouldReturnOptionalPerson() {
        // Given
        String id1 = "firstNameTest1-lastNameTest1";

        // When
        Optional<Person> person = personRepository.findPersonById(id1);

        // Then
        assertTrue(person.isPresent());
        assertEquals("firstNameTest1", person.get().getFirstName());
    }

    @Test
    // On va vérifier ici que la méthode renvoi bien un boolean true si l'id de la Person existe
    void personByIdExists_shouldReturnBoolean() {
        // Given
        String idExist = "firstNameTest1-lastNameTest1";
        String idNotExist = "idNotExist";

        // When / Then
        boolean exists = personRepository.personByIdExists(idExist);
        assertTrue(exists);

        boolean notExists = personRepository.personByIdExists(idNotExist);
        assertFalse(notExists);
    }

    @Test
    // On va vérifier ici que la méthode ajoute correctement une Person
    void addPersonMapping_shouldAddNewPerson() {
        // Given
        Person addPerson = new Person("addPersonFirstName", "addPersonLastName", "addPersonAddress", "addPersonCity", 99, "addPersonPhone", "addPersonEmail");

        // When
        personRepository.addPersonMapping(addPerson);

        // Then
        List<Person> resultPersonList = personRepository.getPersons();
        assertEquals(3, resultPersonList.size());
        assertTrue(resultPersonList.contains(addPerson));
    }

    @Test
    // On va vérifier ici que la méthode renvoi bien le bon Optional<Person> non mis à jour puisque les attributs sont null
    void updatePersonMapping_shouldReturnOptionalPersonUpdatedWhenNullAttributes() {
        // Given
        Person updatePerson = new Person();
        updatePerson.setFirstName("firstNameTest1");
        updatePerson.setLastName("lastNameTest1");

        // When
        Optional<Person> updatePersonOpt = personRepository.updatePersonMapping(updatePerson);

        // Then
        assertTrue(updatePersonOpt.isPresent());
        assertEquals("addressTest1", updatePersonOpt.get().getAddress());
        assertEquals("cityTest1", updatePersonOpt.get().getCity());
        assertEquals(1, updatePersonOpt.get().getZip());
        assertEquals("phoneTest1", updatePersonOpt.get().getPhone());
        assertEquals("emailTest1", updatePersonOpt.get().getEmail());
    }

    @Test
    // On va vérifier ici que la méthode renvoi bien le bon Optional<Person> mis à jour avec ces attributs
    void updatePersonMapping_shouldReturnOptionalPersonUpdated() {
        // Given
        Person updatePerson = new Person();
        updatePerson.setFirstName("firstNameTest1");
        updatePerson.setLastName("lastNameTest1");
        updatePerson.setAddress("updatePersonAddress");
        updatePerson.setCity("updatePersonCity");
        updatePerson.setZip(0);
        updatePerson.setPhone("updatePersonPhone");
        updatePerson.setEmail("updatePersonEmail");

        // When
        Optional<Person> updatePersonOpt = personRepository.updatePersonMapping(updatePerson);

        // Then
        assertTrue(updatePersonOpt.isPresent());
        assertEquals("updatePersonAddress", updatePersonOpt.get().getAddress());
        assertEquals("updatePersonCity", updatePersonOpt.get().getCity());
        assertEquals(0, updatePersonOpt.get().getZip());
        assertEquals("updatePersonPhone", updatePersonOpt.get().getPhone());
        assertEquals("updatePersonEmail", updatePersonOpt.get().getEmail());
    }

    //public void deleteFirestationMapping(Person deletePerson) {
    @Test
    // On va vérifier ici que la méthode supprime bien l'objet demandée (firstName et lastName minimum pour le getId)
    void deleteFirestationMapping_shouldRemovePerson() {
        // Given
        Person deletePerson = new Person("firstNameTest1", "lastNameTest1", null, null, null, null, null);

        // When
        personRepository.deletePersonMapping(deletePerson);

        // Then
        List<Person> resultPersonList = personRepository.getPersons();
        assertEquals(1, resultPersonList.size());
        assertEquals("firstNameTest2", resultPersonList.getFirst().getFirstName());
    }












}
