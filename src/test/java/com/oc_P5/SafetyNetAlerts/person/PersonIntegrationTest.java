package com.oc_P5.SafetyNetAlerts.person;

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

    private final String uriPath = "/person";


    // NOTE Responses possibilities
    //          response 201 ---> Person successfully created
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 409 ---> Conflict: Ressource already exists

    @Test
    void addPerson_shouldReturnHttpStatus201() throws Exception {

        // Given an unknown person to add
        final PersonRequest addPersonRequest = new PersonRequest("firstName", "lastName", "address", "city", 1111, "1234-5678-90", "email@email");

        // When the person is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addPersonRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then response isCreated - 201
        response.andExpect(status().isCreated());
    }

    @Test
    void addPerson_shouldReturnHttpStatus409() throws Exception {

        // Given a known person to add
        final PersonRequest addPersonRequest = new PersonRequest("Foster", "Shepard", "address", "city", 1111, "1234-5678-90", "email@email");

        // When the person is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addPersonRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then response isConflict - 409
        response.andExpect(status().isConflict());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPersonRequest")
    void addPerson_shouldReturnHttpStatus400(PersonRequest addPersonRequest) throws Exception {

        // When the person is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addPersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
    //          response 200 ---> Persons successfully updated
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void updatePerson_shouldReturnHttpStatus200() throws Exception {

        // Given a known person to update
        final PersonRequest updatePersonRequest = new PersonRequest("Tessa", "Carman", "address", "city", 1111, "1234-5678-90", "email@email");

        // When the person is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updatePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

    }

    @Test
    void updatePerson_shouldReturnHttpStatus404() throws Exception {

        // Given an unknown person to update
        final PersonRequest updatePersonRequest = new PersonRequest("unknownFirstName", "unknownLastName", "address", "city", 1111, "1234-5678-90", "email@email");

        // When the person is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updatePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidPersonRequest")
    void updatePerson_shouldReturnHttpStatus400(PersonRequest updatePersonRequest) throws Exception {

        // When person put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updatePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
    //          response 200 ---> Persons successfully deleted
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void deletePerson_shouldReturnHttpStatus200() throws Exception {

        // Given a known person to delete
        final PersonRequest deletePersonRequest = new PersonRequest("John", "Boyd", null, null, null, null, null);

        // When the person is deleted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deletePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

    }

    @Test
    void deletePerson_shouldReturnHttpStatus404() throws Exception {

        // Given an unknown person to delete
        final PersonRequest deletePersonRequest = new PersonRequest("unknownFirstName", "unknownLastName", null, null, null, null, null);

        // When the person is deleted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deletePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidPersonRequest")
    void deletePerson_shouldReturnHttpStatus400(PersonRequest deletePersonRequest) throws Exception {

        // When person posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deletePersonRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
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
