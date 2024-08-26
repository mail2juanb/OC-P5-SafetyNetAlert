package com.oc_P5.SafetyNetAlerts.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.Value;

import java.util.List;

@Value
public class PersonsByStation {
    private final List<PersonByStation> persons;
    private final Integer nbrOfMinors;

    public PersonsByStation(List<Person> persons, Integer nbrOfMinors){

        this.persons = persons.stream().map(PersonByStation::new).toList();
        this.nbrOfMinors = nbrOfMinors;

    }

    @JsonGetter
    public Integer nbrOfMajors(){
        return persons.size() - nbrOfMinors;
    }

    @Value
    static class PersonByStation {
        private final String firstName;
        private final String lastName;
        private final String address;
        private final String phone;

        public PersonByStation(Person person){
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.address = person.getAddress();
            this.phone = person.getPhone();
        }

    }

}
