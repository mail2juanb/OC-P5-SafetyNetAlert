package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.controller.requests.PersonRequest;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.PersonServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonServiceImpl personService;



    /**
     * POST http://localhost:8080/person
     * @param addPersonRequest un object Person contenant : firstName, lastName, address, city, zip, phone, email
     * @return ResponseEntity<>(HttpStatus.CREATED)
     */
    @PostMapping("/person")
    public ResponseEntity<String> addPerson(@Valid @RequestBody PersonRequest addPersonRequest) {

        final Person person = new Person(addPersonRequest);

        personService.addPerson(person);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    /**
     * PUT http://localhost:8080/person
     * @param updatePersonRequest un object Person contenant : *firstName*, *lastName*, address, city, zip, phone, email (* : required)
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PutMapping("/person")
    public ResponseEntity<Void> updatePerson(@Valid @RequestBody PersonRequest updatePersonRequest) {

        final Person person = new Person(updatePersonRequest);

        personService.updatePerson(person);

        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * DELETE http://localhost:8080/person
     * @param deletePersonRequest un object Person contenant : *firstName*, *lastName*, address, city, zip, phone, email (* : required)
     * @return null
     */
    @DeleteMapping("/person")
    public ResponseEntity<Void> deleteFirestation(@Valid @RequestBody PersonRequest deletePersonRequest) {

        final Person person = new Person(deletePersonRequest);

        personService.deletePerson(person);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
