package com.oc_P5.SafetyNetAlerts.dto;

import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.Value;

import java.util.List;

@Value
public class ChildrenByAddress {

    String firstName;
    String lastName;
    int age;
    List<MemberName> familyMembers;


    public ChildrenByAddress(Person person, int age, List<Person> familyMembers) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.age = age;
        this.familyMembers = familyMembers.stream()
                .map(familyMember -> new MemberName(familyMember.getFirstName(), familyMember.getLastName()))
                .toList();
    }

    @Value
    public static class MemberName {
        String firstName;
        String lastName;


        public MemberName(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

}