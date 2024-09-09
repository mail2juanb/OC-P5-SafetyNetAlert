package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PhoneAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final PhoneAlertRepository phoneAlertRepository;
    private final FirestationRepository firestationRepository;

    public List<String> getPhonesByStationNumberService(Integer stationNumber) {
        if(!firestationRepository.firestationByStationExists(stationNumber)) {
            throw new NotFoundException("stationNumber doesn't exists");
        }
        return phoneAlertRepository.getPhonesByStationNumber(stationNumber);
    }

}
