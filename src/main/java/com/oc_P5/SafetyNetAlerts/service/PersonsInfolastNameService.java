package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;

import java.util.List;

public interface PersonsInfolastNameService {

    List<PersonInfoLastName> getPersonsInfolastName(String lastName);
}
