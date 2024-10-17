package com.oc_P5.SafetyNetAlerts.floodStations;

import com.oc_P5.SafetyNetAlerts.controller.FloodStationsController;
import com.oc_P5.SafetyNetAlerts.dto.MemberByStation;
import com.oc_P5.SafetyNetAlerts.service.FloodStationsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Given a list of station_number
        List<Integer> station_Numbers = new ArrayList<>();
        station_Numbers.add(1);

        // When method is called
        ResponseEntity<List<MemberByStation>> result = floodStationsController.getMembersByStation(station_Numbers);

        // Then list of station is sent to service and check HttpStatus.OK
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(floodStationsService, times(1)).getMembersByStation(station_Numbers);

    }

}
