package com.oc_P5.SafetyNetAlerts.dto;

import lombok.Value;

import java.util.List;

@Value
public class FirePersonsResponse {

    List<FirePersonByAddress> firePersonByAdressList;
    String address;
    Integer stationNumber;

}
