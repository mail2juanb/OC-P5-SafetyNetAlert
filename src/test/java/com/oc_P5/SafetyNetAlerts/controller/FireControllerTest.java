package com.oc_P5.SafetyNetAlerts.controller;


import com.oc_P5.SafetyNetAlerts.service.FireServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FireControllerTest {

    @Mock
    FireServiceImpl fireService;

    @InjectMocks
    FireController fireController;


    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getFirePersonsByAddress_shouldReturnFirePersonsResponse() {
        // Given
        String address = "addressTest1";

        // When
        fireController.getFirePersonsByAddress(address);

        // Then
        verify(fireService, times(1)).getFirePersonsByAddress(address);
    }


}
