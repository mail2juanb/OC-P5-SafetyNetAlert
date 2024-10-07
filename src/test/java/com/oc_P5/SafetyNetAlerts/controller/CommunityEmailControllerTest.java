package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.service.CommunityEmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

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
        // Given
        String city = "cityTest1";

        // When
        List<String> result = communityEmailController.getCommunityEmailByCity(city);

        // Then
        verify(communityEmailService, times(1)).getCommunityEmailByCity(city);
        assertEquals(result, Collections.emptyList());
    }

    @Test
    // On va vérifier ici que la méthode du service renvoi une liste vide lorsque la ville n'existe pas
    void getCommunityEmailByCity_shouldReturnEmptyListWhenCityNotExist() {
        // Given
        String city = "unknowCity";

        // When
        List<String> result = communityEmailController.getCommunityEmailByCity(city);

        // Then
        verify(communityEmailService, times(1)).getCommunityEmailByCity(city);
        assertEquals(Collections.emptyList(), result);
    }
}
