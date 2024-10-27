package com.oc_P5.SafetyNetAlerts.phoneAlert;

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

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class PhoneAlertIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private final String uriPath = "/phoneAlert";


    // NOTE Responses possibilities
    //          response 200 ---> Phones successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters

    @Test
    void getPhonesByStationNumber_shouldReturnHttpStatus200WithKnownStation() throws Exception {

        // Given a known stationNumber
        final Integer stationNumber = 2;

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("firestation_number", String.valueOf(stationNumber))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that datas in response are correct
        final String expectedStringList = "[\"841-874-6513\",\"841-874-7878\",\"841-874-7512\",\"841-874-7458\"]";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedStringList);
    }


    @Test
    void getPhonesByStationNumber_shouldReturnHttpStatus200WithUnknownStation() throws Exception {

        // Given an unknown stationNumber
        final Integer stationNumber = 22;

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("firestation_number", String.valueOf(stationNumber))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that datas in response are correct
        final String expectedStringList = "[]";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedStringList);
    }


    @ParameterizedTest
    @MethodSource("provideInvalidStationRequest")
    void getPhonesByStationNumber_shouldReturnHttpStatus400(Integer firestation_number) throws Exception {

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("firestation_number", String.valueOf(firestation_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }



    // Returns invalid station values
    static Stream<Integer> provideInvalidStationRequest() {
        Integer station1 = null;
        Integer station2 = -2;
        Integer station3 = 0;

        return Stream.of(station1, station2, station3);
    }

}
