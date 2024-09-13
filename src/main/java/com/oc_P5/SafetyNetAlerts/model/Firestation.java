package com.oc_P5.SafetyNetAlerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

    private String address;
    private Integer station;

    public Firestation update(Firestation update) {
        if (update.getStation() != null) {
            setStation(update.getStation());
        }
        return this;
    }

}
