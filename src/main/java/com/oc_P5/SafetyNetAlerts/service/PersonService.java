package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.model.Person;

import java.util.List;

public interface PersonService {

    List<Person> getPersons();

    void addPerson(Person addPerson);

    void updatePerson(Person updatePerson);

    void deletePerson(Person deletePerson);

}
