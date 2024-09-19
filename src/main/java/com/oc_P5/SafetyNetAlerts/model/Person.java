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
    private Integer zip;
    private String phone;
    private String email;

    // NOTE Constructeur avec tous les champs mais pourquoi ? L'annotation lombok ne fonctionne pas ?
    // NOTE C'est à cause du super.
    public Person(String firstName, String lastName, String address, String city, Integer zip, String phone, String email) {
        super(firstName, lastName);
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    public Person update(Person update) {
        if(update.getAddress() != null && !update.getAddress().isEmpty()) {
            setAddress(update.getAddress());
        }
        if(update.getCity() != null && !update.getCity().isEmpty()) {
            setCity(update.getCity());
        }
        if(update.getZip() != null) {
            setZip(update.getZip());
        }
        if(update.getPhone() != null && !update.getPhone().isEmpty()) {
            setPhone(update.getPhone());
        }
        if(update.getEmail() != null && update.getEmail().isEmpty()) {
            setEmail(update.getEmail());
        }
        return this;
    }

}
