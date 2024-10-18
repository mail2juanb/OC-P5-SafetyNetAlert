package com.oc_P5.SafetyNetAlerts.communityEmail;

import com.oc_P5.SafetyNetAlerts.controller.CommunityEmailController;
import com.oc_P5.SafetyNetAlerts.service.CommunityEmailServiceImpl;
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
public class CommunityEmailControllerTest {

    @Mock
    CommunityEmailServiceImpl communityEmailService;

    @InjectMocks
    CommunityEmailController communityEmailController;



    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getCommunityEmailByCity_shouldReturnListOfString() {
        // Given a city
        final String city = "cityTest1";

        // When method is called
        ResponseEntity<List<String>> response = communityEmailController.getCommunityEmailByCity(city);

        // Then city is sent to service and check HttpStatus.OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(communityEmailService, times(1)).getCommunityEmailByCity(city);

        ArgumentCaptor<String> cityArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(communityEmailService).getCommunityEmailByCity(cityArgumentCaptor.capture());
        assertThat(cityArgumentCaptor.getValue()).isEqualTo(city);

    }

}