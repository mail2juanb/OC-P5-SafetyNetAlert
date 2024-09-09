package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommunityEmailRepositoryImpl implements CommunityEmailRepository {

    private final PersonRepositoryImpl personRepository;


    public List<String> getCommunityEmailByCity(String city) {
        List<Person> personsByCity = personRepository.getPersonsByCity(city);
        return personsByCity
                .stream()
                .map(Person::getEmail)
                .toList();
    }
}
