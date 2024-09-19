package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;


    public boolean isPersonEmpty(Person person) {
        return (person.getFirstName() == null || person.getFirstName().isEmpty()) &&
                (person.getLastName() == null || person.getLastName().isEmpty()) &&
                (person.getAddress() == null || person.getAddress().isEmpty()) &&
                (person.getCity() == null || person.getCity().isEmpty()) &&
                (person.getZip() == null) &&
                (person.getPhone() == null || person.getPhone().isEmpty()) &&
                (person.getEmail() == null || person.getEmail().isEmpty());
    }

    public List<Person> getPersons() {
        return personRepository.getAll();
    }

    public void addPerson(Person person) {
        if(isPersonEmpty(person)) {
            throw new NullOrEmptyObjectException("Person can not be null or empty");
        }
        if(personRepository.existsById(person.getId())){
            throw new ConflictException("Person already exists for : " + person.getFirstName() + " " + person.getLastName());
        }
        personRepository.save(person);
    }

    public void updatePerson(Person person) {
        if(isPersonEmpty(person)) {
            throw new NullOrEmptyObjectException("Person can not be null or empty");
        }
        Person updatedPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> new NotFoundException("Person doesn't exist with id : " + person.getId()))
                .update(person);
        personRepository.update(updatedPerson);
    }

    public void deletePerson(Person person) {
        if(isPersonEmpty(person)) {
            throw new NullOrEmptyObjectException("Person can not be null or empty");
        }
        if(!personRepository.existsById(person.getId())) {
            throw new NotFoundException("Person doesn't exists with id : " + person.getId());
        }
        personRepository.delete(person);
    }

}
