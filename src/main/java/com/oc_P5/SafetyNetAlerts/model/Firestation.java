package com.oc_P5.SafetyNetAlerts.model;

import com.oc_P5.SafetyNetAlerts.controller.requests.FirestationRequest;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

    private String address;
    private Integer station;


    public Firestation(FirestationRequest request) {
        this.address = request.getAddress();
        this.station = request.getStation_number();
    }

}
