package com.oc_P5.SafetyNetAlerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NamedModel {

    protected String firstName;
    protected String lastName;

    public String getId(){
        return firstName + "-" + lastName;
    }

}
