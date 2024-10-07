package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;


public interface FireService {

    FirePersonsResponse getFirePersonsByAddress(String address);

}
