package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.repository.CommunityEmailRepository;
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
    private final CommunityEmailRepository communityEmailRepository;

    public List<String> getCommunityEmailByCityService(String city) {
        if(city == null || city.trim().isEmpty()) {
            throw new NullOrEmptyObjectException("City can not be null or empty");
        }
        if(!personRepository.existsByCity(city)) {
            throw new NotFoundException("City doesn't exist with city : " + city);
        }
        return communityEmailRepository.getCommunityEmailByCity(city);
    }

}
