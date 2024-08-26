package com.oc_P5.SafetyNetAlerts.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NamedModel {

    protected String firstName;
    protected String lastName;

    public String getId(){
        return firstName + "-" + lastName;
    }

}
