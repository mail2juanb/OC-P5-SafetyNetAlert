package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.service.PersonsInfoLastNameServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PersonsInfoLastNameController {

    private final PersonsInfoLastNameServiceImpl personsInfoLastNameService;

    public PersonsInfoLastNameController(PersonsInfoLastNameServiceImpl personsInfoLastNameService) {
        this.personsInfoLastNameService = personsInfoLastNameService;
    }


    /**
     * GET http://localhost:8080/personInfoLastName=<lastName>
     * @param lastName String lastName
     * @return Liste des personnes portant le lastName demandé.
     */
    @GetMapping
    public List<PersonInfoLastName> getPersonsInfoLastName(@RequestParam("lastName") String lastName) {
        return personsInfoLastNameService.getPersonsInfoLastName(lastName);
    }

}
