package com.oc_P5.SafetyNetAlerts.service;

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
public class CommunityEmailServiceImpl implements CommunityEmailService {

    private final PersonRepository personRepository;

    public List<String> getCommunityEmailByCity(String city) {
        if(StringUtils.isBlank(city)) {
            throw new NullOrEmptyObjectException("City can not be null or empty");
        }
        if(!personRepository.existsByCity(city)) {
            throw new NotFoundException("City doesn't exist with city : " + city);
        }

        return personRepository.getByCity(city)
                .stream()
                .filter(person -> person.getEmail() != null && !person.getEmail().isEmpty())
                .map(Person::getEmail)
                .toList();
    }

}
