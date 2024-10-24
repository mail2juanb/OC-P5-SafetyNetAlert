package com.oc_P5.SafetyNetAlerts.personsInfoLastName;

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
public class PersonsInfoLastNameIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final String uriPath = "/personInfoLastName";


    // NOTE Responses possibilities
    //          response 200 ---> Persons successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void getPersonsInfoLastName_shouldReturnHttpStatus200() throws Exception {

        // Given a lastName
        final String lastName = "Zemicks";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that datas in response are correct
        final String expectedStringResponse = "[{\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"phone\":\"841-874-7878\",\"age\":36,\"medications\":[\"aznol:60mg\",\"hydrapermazol:900mg\",\"pharmacol:5000mg\",\"terazine:500mg\"],\"allergies\":[\"peanut\",\"shellfish\",\"aznol\"]},{\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"phone\":\"841-874-7512\",\"age\":39,\"medications\":[],\"allergies\":[]},{\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"phone\":\"841-874-7512\",\"age\":7,\"medications\":[],\"allergies\":[]}]";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedStringResponse);

    }

    @Test
    void getPersonsInfoLastName_shouldReturnHttpStatus404() throws Exception {

        // Given a lastName
        final String lastName = "unknownLastName";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidLastNameRequest")
    void getPersonsByStation_shouldReturnHttpStatus400(String lastName) throws Exception {

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }



    // Returns invalid lastName values
    static Stream<String> provideInvalidLastNameRequest() {
        return Stream.of("", " ", null);
    }
}