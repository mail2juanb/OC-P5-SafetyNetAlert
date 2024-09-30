package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
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
    public List<Person> getAll() {
        return dataReaderService.getData().getPersons();
    }

    @Override
    public List<Person> getByAddress(String address) {
        return getAll()
                .stream()
                .filter(person -> Objects.equals(person.getAddress(), address))
                .toList();
    }

    @Override
    public List<Person> getByAddresses(Collection<String> addresses) {
        List<Person> personsByAddress = new ArrayList<>();
        addresses.forEach(address -> personsByAddress.addAll(getByAddress(address)));
        return personsByAddress;
    }

    @Override
    public List<Person> getByCity(String city) {
        return getAll()
                .stream()
                .filter(person -> Objects.equals(person.getCity(), city))
                .toList();
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public Optional<Person> findById(String id) {
        return getAll()
                .stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Person> findByCity(String city) {
        return getAll()
                .stream()
                .filter(person -> person.getCity().equals(city))
                .findFirst();
    }

    @Override
    public boolean existsByCity(String city) {
        return findByCity(city).isPresent();
    }

    @Override
    public Optional<Person> findByAddress(String address) {
        return getAll()
                .stream()
                .filter(person -> person.getAddress().equals(address))
                .findFirst();
    }

    @Override
    public boolean existsByLastName(String lastName) {
        return findByLastName(lastName).isPresent();
    }

    @Override
    public Optional<Person> findByLastName(String lastName) {
        return getAll()
                .stream()
                .filter(person -> person.getLastName().equals(lastName))
                .findFirst();
    }

    @Override
    public void save(Person addPerson) {
        List<Person> persons = getAll();
        persons.add(addPerson);
    }

    @Override
    public void update(Person updatePerson) {
        Person personToUpdate = findById(updatePerson.getId()).orElseThrow();
        int index = getAll().indexOf(personToUpdate);
        getAll().set(index, updatePerson);

    }

    @Override
    public void delete(Person deletePerson) {
        List<Person> personList = getAll();
        personList.removeIf(person -> person.getId().equals(deletePerson.getId()));
    }

    @Override
    public List<PersonWithMedicalRecord> getPersonsWithMedicalRecord(List<String> ids ){

        List<Person> personList = getAll()
                .stream()
                .filter(person -> ids.contains(person.getId()))
                .toList();

        List<MedicalRecord> medicalRecordList = dataReaderService.getData().getMedicalRecords()
                .stream()
                .filter(medicalRecord -> ids.contains(medicalRecord.getId()))
                .toList();

        // NOTE Mapper les 2 liste en une seule dans une liste d'objets PersonWithMedicalRecord
        return personList
                .stream()
                .map(person -> mapToPersonWithMedicalRecord(person, medicalRecordList))
                .toList();

    }

    private static PersonWithMedicalRecord mapToPersonWithMedicalRecord(Person person, List<MedicalRecord> medicalRecordList) {
        return medicalRecordList
                .stream()
                .filter(medicalRecord -> medicalRecord.getId().equals(person.getId()))
                .map(medicalRecord -> new PersonWithMedicalRecord(person, medicalRecord))
                .findFirst()
                .orElseThrow();
    }
}
