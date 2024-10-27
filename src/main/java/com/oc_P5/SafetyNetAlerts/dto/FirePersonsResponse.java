package com.oc_P5.SafetyNetAlerts.dto;


import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
public class FirePersonsResponse {

    List<FirePersonByAddress> firePersonByAdressList;
    String address;
    Optional<Integer> stationNumber;


// NOTE: Devrait être supprimé car l'annotation @Value se charge de créer le constructeur

//    public FirePersonsResponse(List<FirePersonByAddress> firePersonByAdressList, String address, Optional<Integer> stationNumber) {
//        this.firePersonByAdressList = firePersonByAdressList;
//        this.address = address;
//        this.stationNumber = stationNumber;
//    }


}
