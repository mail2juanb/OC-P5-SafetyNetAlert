package com.oc_P5.SafetyNetAlerts;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PersonWithMedicalRecordTest {

    private PersonWithMedicalRecord personWithMedicalRecord;

    @BeforeEach
    void setUp() {
        // Test data creation
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        //LocalDate birthdate = LocalDate.parse("09/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate birthdate = LocalDate.now().minusYears(2);
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", birthdate, List.of("Medication1"), List.of("Allergy1"));

        personWithMedicalRecord = new PersonWithMedicalRecord(person, medicalRecord);
    }

    @Test
    void testGetFirstName() {
        assertEquals("John", personWithMedicalRecord.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals("Doe", personWithMedicalRecord.getLastName());
    }

    @Test
    void testIsMinor() {
        assertTrue(personWithMedicalRecord.isMinor()); // Changez la valeur de retour en fonction de l'âge
    }

    @Test
    void testGetAge() {
        assertEquals(2, personWithMedicalRecord.getAge()); // Remplacez par la logique appropriée pour calculer l'âge
    }
}
