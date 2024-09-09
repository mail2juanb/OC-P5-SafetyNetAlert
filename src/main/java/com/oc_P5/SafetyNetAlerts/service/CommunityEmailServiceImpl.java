package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
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
        if(!personRepository.personByCityExists(city)) {
            throw new NotFoundException("city doesn't exist");
        }
        return communityEmailRepository.getCommunityEmailByCity(city);
    }

}
