package com.oc_P5.SafetyNetAlerts.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

    private String address;
    private Integer station;

    public Firestation update(Firestation update) {
            setStation(update.getStation());
        return this;
    }

}
