package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.service.FloodStationsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FloodStationsControllerTest {

    @Mock
    FloodStationsServiceImpl floodStationsService;

    @InjectMocks
    FloodStationsController floodStationsController;


    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getMembersByStation_shouldReturnListOfMemberByStation() {
        // Given
        List<Integer> station_Numbers = new ArrayList<>();
        station_Numbers.add(1);

        // When
        floodStationsController.getMembersByStation(station_Numbers);

        // Then
        verify(floodStationsService, times(1)).getMembersByStation(station_Numbers);
    }

}
