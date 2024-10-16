package com.oc_P5.SafetyNetAlerts.firestation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.controller.requests.FirestationRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FirestationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    // NOTE PERSONS_BY_STATION
    //          response 200 ---> Persons successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void getPersonsByStation_shouldReturnHttpStatus200() throws Exception {

        // Given a station
        final Integer station_number = 2;
        final String uriPath = "/firestation";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isOk());

    }

    @Test
    void getPersonsByStation_shouldReturnHttpStatus404() throws Exception {

        // Given a station
        final Integer station_number = 10;
        final String uriPath = "/firestation";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isNotFound());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidStationRequest")
    void getPersonsByStation_shouldReturnHttpStatus400(Integer station_number) throws Exception {

        // Given uriPath
        final String uriPath = "/firestation";

        // When Firestation posted
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }


    // NOTE ADD / POST
    //          response 201 ---> Firestation successfully added
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 409 ---> Conflict: Firestation already exists

    @Test
    void addFirestation_shouldReturnHttpStatus201() throws Exception {

        // Given a firestation to add
        final FirestationRequest addFirestationRequest = new FirestationRequest("addAddress", 9);

        final String uriPath = "/firestation";

        // When the firestation is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isCreated());

    }


    @Test
    void addFirestation_shouldReturnHttpStatus409() throws Exception {

        // Given a firestation to add
        final FirestationRequest addFirestationRequest = new FirestationRequest("834 Binoc Ave", 3);

        final String uriPath = "/firestation";

        // When the firestation is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isConflict());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidFirestationRequest")
    void addFirestation_shouldReturnHttpStatus400(FirestationRequest addFirestationRequest) throws Exception {

        // Given postAddress
        final String uriPath = "/firestation";

        // When the firestation is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }


    // NOTE UPDATE / PUT
    //          response 200 ---> Firestation successfully updated
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void updateFirestation_shouldReturnHttpStatus200() throws Exception {

        // Given a firestation to update
        final FirestationRequest updateFirestationRequest = new FirestationRequest("112 Steppes Pl", 9);

        final String uriPath = "/firestation";

        // When the firestation is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isOk());

    }


    @Test
    void updateFirestation_shouldReturnHttpStatus404() throws Exception {

        // Given a firestation to update
        final FirestationRequest updateFirestationRequest = new FirestationRequest("unknownAddress", 9);

        final String uriPath = "/firestation";

        // When the firestation is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isNotFound());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidFirestationRequest")
    void updateFirestation_shouldReturnHttpStatus400(FirestationRequest updateFirestationRequest) throws Exception {

        // Given postAddress
        final String uriPath = "/firestation";

        // When Firestation posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }


    // NOTE REMOVE / DELETE by address
    //          response 200 ---> Firestation successfully deleted
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void deleteFirestationByAddress_shouldReturnReturnHttpStatus200() throws Exception {

        // Given an address to delete Firestation
        final String address = "1509 Culver St";
        final String uriPath = "/firestation/address";

        // When Firestation deleted
        ResultActions response = mockMvc.perform(delete(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isOk());

    }


    @Test
    void deleteFirestationByAddress_shouldReturnReturnHttpStatus404() throws Exception {

        // Given an address to delete Firestation
        final String address = "unknownAddress";
        final String uriPath = "/firestation/address";

        // When Firestation deleted
        ResultActions response = mockMvc.perform(delete(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isNotFound());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidAddressRequest")
    void deleteFirestationByAddress_shouldReturnHttpStatus400(String address) throws Exception {

        // Given uriPath
        final String uriPath = "/firestation/address";

        // When Firestation posted
        ResultActions response = mockMvc.perform(delete(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }


    // NOTE REMOVE / DELETE by station
    //          response 200 ---> Firestations successfully deleted
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void deleteFirestationByStation_shouldReturnReturnHttpStatus200() throws Exception {

        // Given a station to delete Firestation
        final Integer station_number = 1;
        final String deleteStation = "/firestation/station";

        // When the Firestation deleted
        ResultActions response = mockMvc.perform(delete(deleteStation)
                        .param("station_number", String.valueOf(station_number))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isOk());

    }


    @Test
    void deleteFirestationByStation_shouldReturnReturnHttpStatus404() throws Exception {

        // Given a station to delete Firestation
        final Integer station_number = 9;
        final String deleteStation = "/firestation/station";

        // When the Firestation deleted
        ResultActions response = mockMvc.perform(delete(deleteStation)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isNotFound());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidStationRequest")
    void deleteFirestationByStation_shouldReturnHttpStatus400(Integer station_number) throws Exception {

        // Given uriPath
        final String uriPath = "/firestation/station";

        // When Firestation posted
        ResultActions response = mockMvc.perform(delete(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }



    // Returns invalid FirestationRequest values
    static Stream<FirestationRequest> provideInvalidFirestationRequest() {
        FirestationRequest firestation1 = new FirestationRequest("", 1);
        FirestationRequest firestation2 = new FirestationRequest(" ", 1);
        FirestationRequest firestation3 = new FirestationRequest(null, 1);
        FirestationRequest firestation4 = new FirestationRequest("address", null);
        FirestationRequest firestation5 = new FirestationRequest("address", 0);
        FirestationRequest firestation6 = new FirestationRequest("address", -1);

        return Stream.of(firestation1, firestation2, firestation3, firestation4, firestation5, firestation6);
    }

    // Returns invalid address values
    static Stream<String> provideInvalidAddressRequest() {
        String address1 = "";
        String address2 = " ";
        String address3 = null;

        return Stream.of(address1, address2, address3);
    }

    // Returns invalid station values
    static Stream<Integer> provideInvalidStationRequest() {
        Integer station1 = null;
        Integer station2 = -2;
        Integer station3 = 0;

        return Stream.of(station1, station2, station3);
    }

}