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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
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
    void getMembersByStation_shouldReturnHttpStatus200WithKnownStations() throws Exception {

        // Given a list of station
        final List<Integer> stationNumbers = new ArrayList<>();
        stationNumbers.add(1);
        stationNumbers.add(2);

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("stationNumbers", stationNumbers.stream()
                        .map(String::valueOf) // Convert each Integer to String
                        .toArray(String[]::new)) // Convert to array of string
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check datas in response
        String expectedResponse = "[{\"lastName\":\"Marrack\",\"address\":\"29 15th St\",\"phone\":\"841-874-6513\",\"age\":35,\"medications\":[],\"allergies\":[]}," +
                "{\"lastName\":\"Duncan\",\"address\":\"644 Gershwin Cir\",\"phone\":\"841-874-6512\",\"age\":24,\"medications\":[],\"allergies\":[\"shellfish\"]}," +
                "{\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"phone\":\"841-874-7878\",\"age\":36,\"medications\":[\"aznol:60mg\",\"hydrapermazol:900mg\",\"pharmacol:5000mg\",\"terazine:500mg\"],\"allergies\":[\"peanut\",\"shellfish\",\"aznol\"]}," +
                "{\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"phone\":\"841-874-7512\",\"age\":39,\"medications\":[],\"allergies\":[]}," +
                "{\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"phone\":\"841-874-7512\",\"age\":7,\"medications\":[],\"allergies\":[]}," +
                "{\"lastName\":\"Walker\",\"address\":\"908 73rd St\",\"phone\":\"841-874-8547\",\"age\":45,\"medications\":[\"thradox:700mg\"],\"allergies\":[\"illisoxian\"]}," +
                "{\"lastName\":\"Peters\",\"address\":\"908 73rd St\",\"phone\":\"841-874-7462\",\"age\":42,\"medications\":[],\"allergies\":[]}," +
                "{\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"phone\":\"841-874-7784\",\"age\":48,\"medications\":[\"ibupurin:200mg\",\"hydrapermazol:400mg\"],\"allergies\":[\"nillacilan\"]}," +
                "{\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"phone\":\"841-874-7784\",\"age\":44,\"medications\":[],\"allergies\":[]}," +
                "{\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"phone\":\"841-874-7784\",\"age\":10,\"medications\":[\"noxidian:100mg\",\"pharmacol:2500mg\"],\"allergies\":[]}," +
                "{\"lastName\":\"Cadigan\",\"address\":\"951 LoneTree Rd\",\"phone\":\"841-874-7458\",\"age\":79,\"medications\":[\"tradoxidine:400mg\"],\"allergies\":[]}]";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedResponse);

    }


    @Test
    void getMembersByStation_shouldReturnHttpStatus200WithUnknownStations() throws Exception {

        // Given a list of station
        final List<Integer> stationNumbers = new ArrayList<>();
        stationNumbers.add(44);
        stationNumbers.add(52);

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("stationNumbers", stationNumbers.stream()
                        .map(String::valueOf) // Convert each Integer to String
                        .toArray(String[]::new)) // Convert to array of string
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check datas in response
        String expectedResponse = "[]";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedResponse);

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

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }



    // Returns invalid station_Numbers values
    static Stream<List<Integer>> provideInvalidStation_Numbers() {
        return Stream.of(Arrays.asList(-1, null), Arrays.asList(1, null, 2, 9));
    }

}