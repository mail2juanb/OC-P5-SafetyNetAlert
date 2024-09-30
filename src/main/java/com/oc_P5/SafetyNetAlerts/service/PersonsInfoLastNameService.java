package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;

import java.util.List;

public interface PersonsInfoLastNameService {

    List<PersonInfoLastName> getPersonsInfoLastName(String lastName);
}
