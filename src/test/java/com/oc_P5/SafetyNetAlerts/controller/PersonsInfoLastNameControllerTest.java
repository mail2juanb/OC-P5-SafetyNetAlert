package com.oc_P5.SafetyNetAlerts.controller;


import com.oc_P5.SafetyNetAlerts.service.PersonsInfoLastNameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonsInfoLastNameControllerTest {

    @Mock
    PersonsInfoLastNameServiceImpl personsInfoLastNameService;

    @InjectMocks
    PersonsInfoLastNameController personsInfoLastNameController;


    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getPersonsInfoLastName_shouldReturnListOfPersonInfoLastName() {
        // Given
        String lastName = "lastNameTest1";

        // When
        personsInfoLastNameController.getPersonsInfoLastName(lastName);

        // Then
        verify(personsInfoLastNameService, times(1)).getPersonsInfoLastName(lastName);
    }
}
