package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.PersonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PersonController {

    private final PersonServiceImpl personService;

    public PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }


    /**
     * GET http://localhost:8080/persons
     * @return Liste des Person ainsi que leurs attributs
     */
    @GetMapping("/persons")
    public List<Person> getPersonsController() {
        return personService.getPersons();
    }


    /**
     * POST http://localhost:8080/person
     * @param person un object Person contenant : firstName, lastName, address, city, zip, phone, email
     * @return ResponseEntity<>(HttpStatus.CREATED)
     */
    @PostMapping("/person")
    public ResponseEntity<String> addPersonController(@RequestBody Person person) {
        personService.addPerson(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * PUT http://localhost:8080/person
     * @param person un object Person contenant : *firstName*, *lastName*, address, city, zip, phone, email (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PutMapping("/person")
    public ResponseEntity<String> updatePersonController(@RequestBody Person person) {
        personService.updatePerson(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * DELETE http://localhost:8080/person
     * @param person un object Person contenant : *firstName*, *lastName*, address, city, zip, phone, email (* : required)
     * @return null
     */
    @DeleteMapping("/person")
    public ResponseEntity<String> deleteFirestationController(@RequestBody Person person) {
        personService.deletePerson(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
