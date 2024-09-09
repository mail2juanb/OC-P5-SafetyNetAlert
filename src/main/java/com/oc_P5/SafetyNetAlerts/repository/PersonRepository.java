package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Person;

import java.util.Collection;
import java.util.Optional;
import java.util.List;

public interface PersonRepository {

    List<Person> getPersons();

    List<Person> getPersonsByAddress(String address);

    List<Person> getPersonsByAddress(Collection<String> address);

    //Optional<Person> findPersonByName(String firstName, String lastName);

    Optional<Person> findPersonById(String id);

    boolean personByIdExists(String id);

    void addPersonMapping(Person addPerson);

    Optional<Person> updatePersonMapping(Person updatePerson);

    void deletePersonMapping(Person deletePerson);

    Optional<Person> findPersonByCity(String city);

    boolean personByCityExists(String city);

    List<Person> getPersonsByCity(String city);

}
