package com.oc_P5.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.controller.requests.PersonRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void addPerson_shouldReturnResponseEntity() throws Exception {

        // Given a person to add
        final PersonRequest addPersonRequest = new PersonRequest("firstName", "lastName", "address", "city", 1111, "1234-5678-90", "email@email");

        final String uriPath = "/person";

        // When the person is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addPersonRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then the person is sent to the service
        response.andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPersonRequest")
    void addPerson_shouldThrowException(PersonRequest addPersonRequest) throws Exception {

        // Given a person to add
        final String uriPath = "/person";

        // When the person is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addPersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the person is sent to the service
        response.andExpect(status().isBadRequest());

    }

    @Test
    void updatePerson_shouldReturnResponseEntity() throws Exception {

        // Given a person to update
        final PersonRequest updatePersonRequest = new PersonRequest("Tessa", "Carman", "address", "city", 1111, "1234-5678-90", "email@email");

        final String uriPath = "/person";

        // When the person is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updatePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the person is sent to the service
        response.andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidPersonRequest")
    void updatePerson_shouldThrowException(PersonRequest updatePersonRequest) throws Exception {

        // Given path
        final String uriPath = "/person";

        // When person posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updatePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the person is sent to the service
        response.andExpect(status().isBadRequest());

    }

    @Test
    void deletePerson_shouldReturnResponseEntity() throws Exception {

        // Given a person to delete
        final PersonRequest deletePersonRequest = new PersonRequest("John", "Boyd", null, null, null, null, null);

        final String uriPath = "/person";

        // When the person is deleted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deletePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the person is sent to the service
        response.andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidPersonRequest")
    void deletePerson_shouldThrowException(PersonRequest deletePersonRequest) throws Exception {

        // Given path
        final String uriPath = "/person";

        // When person posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deletePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the person is sent to the service
        response.andExpect(status().isBadRequest());

    }



    // Fournit des valeurs de PersonRequest, y compris null
    static Stream<PersonRequest> provideInvalidPersonRequest() {
        final PersonRequest personRequest1 = new PersonRequest("", "", "", "", null, "", "");
        final PersonRequest personRequest2 = new PersonRequest(null, null, null, null, null, null, null);
        final PersonRequest personRequest3 = new PersonRequest("firstName", "lastName", "address", "city", 1234, "1234567", "email");

        return Stream.of(personRequest1, personRequest2, personRequest3);
    }


}
