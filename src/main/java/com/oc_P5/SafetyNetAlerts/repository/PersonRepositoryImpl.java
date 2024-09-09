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
    public List<Person> getPersonsByAddresses(Collection<String> addresses) {
        List<Person> personsByAddress = new ArrayList<>();
        addresses.forEach(address -> personsByAddress.addAll(getPersonsByAddress(address)));
        return personsByAddress;
    }

    @Override
    public List<Person> getPersonsByCity(String city) {
        return getPersons()
                .stream()
                .filter(person -> Objects.equals(person.getCity(), city))
                .toList();
    }

//    @Override
//    public Optional<Person> findPersonByName(String firstName, String lastName) {
//        return  getPersons()
//                .stream()
//                .filter(person -> person.getFirstName().equals(firstName))
//                .filter(person -> person.getLastName().equals(lastName))
//                .findFirst();
//    }

    @Override
    public boolean personByIdExists(String id) {
        return findPersonById(id).isPresent();
    }

    @Override
    public Optional<Person> findPersonById(String id) {
        return getPersons()
                .stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Person> findPersonByCity(String city) {
        return getPersons()
                .stream()
                .filter(person -> person.getCity().equals(city))
                .findFirst();
    }

    @Override
    public boolean personByCityExists(String city) {
        return findPersonByCity(city).isPresent();
    }

    @Override
    public void addPersonMapping(Person addPerson) {
        List<Person> persons = getPersons();
        persons.add(addPerson);
    }

    @Override
    public Optional<Person> updatePersonMapping(Person updatePerson) {
        return findPersonById(updatePerson.getId())
                .map(person -> {
                    if(updatePerson.getAddress() != null) {
                        person.setAddress(updatePerson.getAddress());
                    }
                    if(updatePerson.getCity() != null) {
                        person.setCity(updatePerson.getCity());
                    }
                    if(updatePerson.getZip() != null) {
                        person.setZip(updatePerson.getZip());
                    }
                    if(updatePerson.getPhone() != null) {
                        person.setPhone(updatePerson.getPhone());
                    }
                    if(updatePerson.getEmail() != null) {
                        person.setEmail(updatePerson.getEmail());
                    }

                    return person;
                });
    }

    @Override
    public void deletePersonMapping(Person deletePerson) {
        List<Person> persons = getPersons();
        persons.removeIf(person -> person.getId().equals(deletePerson.getId()));
    }


}
