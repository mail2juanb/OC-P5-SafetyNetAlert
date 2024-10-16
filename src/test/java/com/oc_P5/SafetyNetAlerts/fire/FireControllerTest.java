package com.oc_P5.SafetyNetAlerts.fire;


import com.oc_P5.SafetyNetAlerts.controller.FireController;
import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.service.FireServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FireControllerTest {

    @Mock
    FireServiceImpl fireService;

    @InjectMocks
    FireController fireController;


    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getFirePersonsByAddress_shouldReturnFirePersonsResponse() {
        // Given an address
        String address = "addressTest1";

        // When method is called
        ResponseEntity<FirePersonsResponse> response = fireController.getFirePersonsByAddress(address);

        // Then address is sent to service and check HttpStatus.OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(fireService, times(1)).getFirePersonsByAddress(address);

        ArgumentCaptor<String> addressArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(fireService).getFirePersonsByAddress(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);
    }

}