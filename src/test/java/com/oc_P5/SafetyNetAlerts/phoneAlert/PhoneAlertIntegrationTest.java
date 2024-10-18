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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PhoneAlertIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private final String uriPath = "/phoneAlert";


    // NOTE Responses possibilities
    //          response 200 ---> Phones successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void getPhonesByStationNumber_shouldReturnHttpStatus200() throws Exception {

        // Given a firestation_number
        final Integer firestation_number = 2;

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("firestation_number", String.valueOf(firestation_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

    }


    @Test
    void getPhonesByStationNumber_shouldReturnHttpStatus404() throws Exception {

        // Given a firestation_number
        final Integer firestation_number = 89;

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("firestation_number", String.valueOf(firestation_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

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
