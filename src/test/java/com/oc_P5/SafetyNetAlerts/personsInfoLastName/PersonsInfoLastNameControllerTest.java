package com.oc_P5.SafetyNetAlerts.personsInfoLastName;


import com.oc_P5.SafetyNetAlerts.controller.PersonsInfoLastNameController;
import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.service.PersonsInfoLastNameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class PersonsInfoLastNameControllerTest {

    @Mock
    PersonsInfoLastNameServiceImpl personsInfoLastNameService;

    @InjectMocks
    PersonsInfoLastNameController personsInfoLastNameController;


    @Test
    void getPersonsInfoLastName_shouldReturnListOfPersonInfoLastName() {
        // Given a lastName
        final String lastName = "lastNameTest1";

        // When method is called
        ResponseEntity<List<PersonInfoLastName>> response = personsInfoLastNameController.getPersonsInfoLastName(lastName);

        // Then station is sent to service and check HttpStatus.OK
        ArgumentCaptor<String> lastNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(personsInfoLastNameService).getPersonsInfoLastName(lastNameArgumentCaptor.capture());
        assertThat(lastNameArgumentCaptor.getValue()).isEqualTo(lastName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}