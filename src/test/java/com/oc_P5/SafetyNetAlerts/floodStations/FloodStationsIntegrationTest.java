package com.oc_P5.SafetyNetAlerts.floodStations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FloodStationsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private final String uriPath = "/flood/stations";



    // NOTE Responses possibilities
    //          response 200 ---> Persons successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters

    @Test
    void getMembersByStation_shouldReturnHttpStatus200() throws Exception {

        // Given a list of station
        final List<Integer> station_Numbers = new ArrayList<>();
        station_Numbers.add(1);
        station_Numbers.add(2);

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_Numbers", station_Numbers.stream()
                        .map(String::valueOf) // Convert each Integer to String
                        .toArray(String[]::new)) // Convert to array of string
                .contentType(MediaType.APPLICATION_JSON));

        // Then the list of station_Number is sent to the service and status isOk
        response.andExpect(status().isOk());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidStation_Numbers")
    void getMembersByStation_shouldReturnHttpStatus400(List<Integer> station_Numbers) throws Exception {

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_Numbers", station_Numbers.stream()
                        .map(String::valueOf) // Convert each Integer to String
                        .toArray(String[]::new)) // Convert to array of string
                .contentType(MediaType.APPLICATION_JSON));

        // Then the list of station_Number is sent to the service and status isBadRequest
        response.andExpect(status().isBadRequest());

    }



    // Fournit des valeurs de station_Numbers, y compris null
    static Stream<List<Integer>> provideInvalidStation_Numbers() {
        return Stream.of(Arrays.asList(-1, null), Arrays.asList(1, null, 2, 9));
    }

}