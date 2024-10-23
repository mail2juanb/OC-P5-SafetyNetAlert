package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityEmailServiceImpl implements CommunityEmailService {

    private final PersonRepository personRepository;

    @Override
    public List<String> getCommunityEmailByCity(String city) {
        if(!personRepository.existsByCity(city)) {
            throw new NotFoundException("City doesn't exist with city : " + city);
        }

        return personRepository.getByCity(city)
                .stream()
                .map(Person::getEmail)
                .distinct()
                .toList();
    }

}