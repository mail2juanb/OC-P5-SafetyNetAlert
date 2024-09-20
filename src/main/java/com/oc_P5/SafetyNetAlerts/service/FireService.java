package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsByAddress;

import java.util.List;

public interface FireService {

    FirePersonsByAddress getFirePersonsByAddress(String address);

}
