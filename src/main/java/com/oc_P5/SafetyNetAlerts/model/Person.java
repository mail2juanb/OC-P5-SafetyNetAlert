package com.oc_P5.SafetyNetAlerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person extends NamedModel {

    private String address;
    private String city;
    private int zip;
    private String phone;
    private String email;

    // FIXME Constructeur avec tous les champs mais pourquoi ? L'annotation lombok ne fonctionne pas ?
    public Person(String firstName, String lastName, String address, String city, int zip, String phone, String email) {
        super(firstName, lastName);
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

}
