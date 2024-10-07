package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;


    @Override
    public List<Person> getPersons() {
        return personRepository.getAll();
    }

    @Override
    public void addPerson(Person person) {
        if(isPersonEmpty(person)) {
            throw new NullOrEmptyObjectException("Person can not be null or empty");
        }
        if(personRepository.existsById(person.getId())){
            throw new ConflictException("Person already exists for : " + person.getFirstName() + " " + person.getLastName());
        }
        personRepository.save(person);
    }

    @Override
    public void updatePerson(Person person) {
        if(isPersonEmpty(person)) {
            throw new NullOrEmptyObjectException("Person can not be null or empty");
        }
        Person updatedPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> new NotFoundException("Person doesn't exist with id : " + person.getId()))
                .update(person);
        personRepository.update(updatedPerson);
    }

    @Override
    public void deletePerson(Person person) {
        if(isPersonEmpty(person)) {
            throw new NullOrEmptyObjectException("Person can not be null or empty");
        }
        if(!personRepository.existsById(person.getId())) {
            throw new NotFoundException("Person doesn't exists with id : " + person.getId());
        }
        personRepository.delete(person);
    }


    public boolean isPersonEmpty(Person person) {
        return (StringUtils.isBlank(person.getFirstName())) &&
                (StringUtils.isBlank(person.getLastName())) &&
                (StringUtils.isBlank(person.getAddress())) &&
                (StringUtils.isBlank(person.getCity())) &&
                (person.getZip() == null) &&
                (StringUtils.isBlank(person.getPhone())) &&
                (StringUtils.isBlank(person.getPhone()));
    }


}