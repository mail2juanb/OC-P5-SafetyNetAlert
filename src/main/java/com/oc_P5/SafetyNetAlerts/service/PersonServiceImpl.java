package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;



    @Override
    public void addPerson(Person person) {
        if(personRepository.existsById(person.getId())){
            throw new ConflictException("Person already exists for : " + person.getFirstName() + " " + person.getLastName());
        }
        personRepository.save(person);
    }

    @Override
    public void updatePerson(Person person) {
        Person updatedPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> new NotFoundException("Person doesn't exist with id : " + person.getId()));
        personRepository.update(updatedPerson);
    }

    @Override
    public void deletePerson(Person person) {
        if(!personRepository.existsById(person.getId())) {
            throw new NotFoundException("Person doesn't exists with id : " + person.getId());
        }
        personRepository.delete(person);
    }

}