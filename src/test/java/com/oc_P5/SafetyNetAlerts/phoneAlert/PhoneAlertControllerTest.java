package com.oc_P5.SafetyNetAlerts.phoneAlert;

import com.oc_P5.SafetyNetAlerts.controller.PhoneAlertController;
import com.oc_P5.SafetyNetAlerts.service.PhoneAlertServiceImpl;
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
        // Given a firestation_number
        final Integer firestation_number = 1;

        // When method is called
        ResponseEntity<List<String>> phonesByStationNumber = phoneAlertController.getPhonesByStationNumber(firestation_number);

        // Then station is sent to service and check HttpStatus.OK
        ArgumentCaptor<Integer> stationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(phoneAlertService).getPhonesByStation(stationArgumentCaptor.capture());
        assertThat(stationArgumentCaptor.getValue()).isEqualTo(firestation_number);

        assertEquals(HttpStatus.OK, phonesByStationNumber.getStatusCode());
        verify(phoneAlertService, times(1)).getPhonesByStation(firestation_number);
    }

}