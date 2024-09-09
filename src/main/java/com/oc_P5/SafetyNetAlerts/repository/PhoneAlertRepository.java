package com.oc_P5.SafetyNetAlerts.repository;

import java.util.List;

public interface PhoneAlertRepository {

    List<String> getPhonesByStationNumber(Integer stationNumber);

}
