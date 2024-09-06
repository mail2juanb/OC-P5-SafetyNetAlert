package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository{
    private final DataReader dataReaderService;


    @Override
    public List<Person> getPersons() {
        return dataReaderService.getData().getPersons();
    }

    @Override
    public List<Person> getPersonsByAddress(String address) {
        return getPersons()
                .stream()
                .filter(person -> Objects.equals(person.getAddress(), address))
                .toList();
    }

    @Override
    public List<Person> getPersonsByAddress(Collection<String> addresses) {
        List<Person> personsByAddress = new ArrayList<>();
        addresses.forEach(address -> personsByAddress.addAll(getPersonsByAddress(address)));
        return personsByAddress;
    }

    @Override
    public Optional<Person> findPersonByName(String firstName, String lastName) {
        return  getPersons()
                .stream()
                .filter(person -> person.getFirstName().equals(firstName))
                .filter(person -> person.getLastName().equals(lastName))
                .findFirst();
    }

    @Override
    public Optional<Person> findPersonById(String id) {
        return getPersons()
                .stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }
}
