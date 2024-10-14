package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.model.Person;


public interface PersonService {

    void addPerson(Person addPerson);

    void updatePerson(Person updatePerson);

    void deletePerson(Person deletePerson);

}
