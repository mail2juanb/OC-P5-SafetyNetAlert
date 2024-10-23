package com.oc_P5.SafetyNetAlerts.fire;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FireIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final String uriPath = "/fire";


    // NOTE Responses possibilities
    //          response 200 ---> Persons successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void getFirePersonsByAddress_shouldReturnHttpStatus200() throws Exception {

        // Given an address
        final String address = "112 Steppes Pl";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOK - 200
        response.andExpect(status().isOk());

        // Then check datas in the response
        String expectedResponse = "{\"firePersonByAdressList\":[{\"lastName\":\"Cooper\",\"phone\":\"841-874-6874\",\"age\":30,\"medications\":[\"hydrapermazol:300mg\",\"dodoxadin:30mg\"],\"allergies\":[\"shellfish\"]},{\"lastName\":\"Peters\",\"phone\":\"841-874-8888\",\"age\":59,\"medications\":[],\"allergies\":[]},{\"lastName\":\"Boyd\",\"phone\":\"841-874-9888\",\"age\":59,\"medications\":[\"aznol:200mg\"],\"allergies\":[\"nillacilan\"]}],\"address\":\"112 Steppes Pl\",\"stationNumber\":3}";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedResponse);

    }


    @Test
    void getFirePersonsByAddress_shouldReturnHttpStatus404() throws Exception {

        // Given an unknown address
        final String address = "unknown address";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidAddressRequest")
    void getFirePersonsByAddress_shouldReturnHttpStatus400(String address) throws Exception {

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }



    // Returns invalid address values
    static Stream<String> provideInvalidAddressRequest() {
        String address1 = "";
        String address2 = " ";
        String address3 = null;

        return Stream.of(address1, address2, address3);
    }
}
