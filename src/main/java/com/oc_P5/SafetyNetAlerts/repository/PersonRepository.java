package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Person;

import java.util.Collection;
import java.util.Optional;
import java.util.List;

public interface PersonRepository {

    List<Person> getAll();

    List<Person> getByAddress(String address);

    List<Person> getByAddresses(Collection<String> address);

    Optional<Person> findById(String id);

    boolean existsById(String id);

    boolean existsByLastName(String lastName);

    Optional<Person> findByLastName(String lastName);

    void save(Person addPerson);

    void update(Person updatePerson);

    void delete(Person deletePerson);

    Optional<Person> findByCity(String city);

    boolean existsByCity(String city);

    List<Person> getByCity(String city);

    Optional<Person> findByAddress(String address);

}
