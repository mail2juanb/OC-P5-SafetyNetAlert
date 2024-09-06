package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> getPersonsService();

    void addPersonMappingService(Person addPerson);

    void updatePersonMappingService(Person updatePerson);

    void deletePersonMappingService(Person deletePerson);

}
