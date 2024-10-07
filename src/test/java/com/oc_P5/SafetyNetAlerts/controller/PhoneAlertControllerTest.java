package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.service.PhoneAlertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PhoneAlertControllerTest {

    @Mock
    PhoneAlertServiceImpl phoneAlertService;

    @InjectMocks
    PhoneAlertController phoneAlertController;


    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getPhonesByStationNumber_shouldReturnListOfString() {
        // Given
        Integer stationNumber = 1;

        // When
        phoneAlertController.getPhonesByStationNumber(stationNumber);

        // Then
        verify(phoneAlertService, times(1)).getPhonesByStation(stationNumber);
    }

}