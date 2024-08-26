package com.oc_P5.SafetyNetAlerts.model;

import lombok.Getter;

@Getter
public class Person extends NamedModel{

    private String address;
    private String city;
    private int zip;
    private String phone;
    private String email;

}
