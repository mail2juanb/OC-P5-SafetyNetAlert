package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.ConflictException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public List<Person> getPersonsService() {
        return personRepository.getPersons();
    }

    public void addPersonMappingService(Person addPerson) {
        if(personRepository.personByIdExists(addPerson.getId())){
            throw new ConflictException("addPerson already exists");
        }
        personRepository.addPersonMapping(addPerson);
    }

    // TODO Que ce passe t il si les attributs firstName et lastName sont null. Cela empècherai le fonctionnement ?
    public void updatePersonMappingService(Person updatePerson) {
        if(updatePerson == null || !personRepository.personByIdExists(updatePerson.getId())) {
            throw new ConflictException("updatePerson doesn't exists");
        }
        personRepository.updatePersonMapping(updatePerson);
    }

    // TODO Que ce passe t il si les attributs firstName et lastName sont null. Cela empècherai le fonctionnement ?
    public void deleteFirestationMappingService(Person deletePerson) {
        if(deletePerson == null || !personRepository.personByIdExists(deletePerson.getId())) {
            throw new ConflictException("deletePerson doesn't exists");
        }
        personRepository.deleteFirestationMapping(deletePerson);
    }
}
