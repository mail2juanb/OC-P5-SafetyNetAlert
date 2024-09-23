package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonsInfolastName;
import com.oc_P5.SafetyNetAlerts.service.PersonsInfolastNameServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PersonsInfolastNameController {

    private final PersonsInfolastNameServiceImpl personsInfolastNameService;

    public PersonsInfolastNameController(PersonsInfolastNameServiceImpl personsInfolastNameService) {
        this.personsInfolastNameService = personsInfolastNameService;
    }


    /**
     * GET http://localhost:8080/personInfolastName=<lastName>
     * @param lastName String lastName
     * @return Liste des personnes portant le lastName demandé.
     * mise en forme : String lastName, String address, String phone, int age, List<String>medications, List<String>allergies
     */
    @GetMapping("")
    public PersonsInfolastName getPersonsInfolastName(@RequestParam("lastName") String lastName) {
        return personsInfolastNameService.getPersonsInfolastName(lastName);
    }

}
