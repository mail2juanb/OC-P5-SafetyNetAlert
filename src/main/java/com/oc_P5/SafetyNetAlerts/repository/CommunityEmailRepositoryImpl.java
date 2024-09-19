package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommunityEmailRepositoryImpl implements CommunityEmailRepository {

    private final PersonRepositoryImpl personRepository;

    public List<String> getCommunityEmailByCity(String city) {
        List<Person> personsByCity = personRepository.getByCity(city);
        Set<String> uniquesCommunityEmailByCity = personsByCity
                .stream()
                .map(Person::getEmail)
                .collect(Collectors.toSet());
        return new ArrayList<>(uniquesCommunityEmailByCity);
    }

}
