package com.oc_P5.SafetyNetAlerts.service;

import java.util.List;

public interface PhoneAlertService {

    List<String> getPhonesByStationNumberService(Integer stationNumber);

}
