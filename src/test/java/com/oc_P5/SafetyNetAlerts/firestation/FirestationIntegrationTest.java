package com.oc_P5.SafetyNetAlerts.firestation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.controller.requests.FirestationRequest;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
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

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Autowired
    private FirestationRepository repository;

    private final String uriPath = "/firestation";



    // NOTE Responses possibilities
    //          response 200 ---> Persons successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void getPersonsByStation_shouldReturnHttpStatus200() throws Exception {

        // Given a station
        final Integer station_number = 4;

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that all addresses are present in the response
        String strResponse = response.andReturn().getResponse().getContentAsString();
        final List<String> stationAddressList = repository.getByStation(station_number)
                .stream()
                .map(Firestation::getAddress)
                .toList();

        for (String address : stationAddressList) {
            assertThat(strResponse).contains(address);
        }
    }

    @Test
    void getPersonsByStation_shouldReturnHttpStatus404() throws Exception {

        // Given a station
        final Integer station_number = 10;

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidStationRequest")
    void getPersonsByStation_shouldReturnHttpStatus400(Integer station_number) throws Exception {

        // When Firestation posted
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
    //          response 201 ---> Firestation successfully added
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 409 ---> Conflict: Firestation already exists

    @Test
    void addFirestation_shouldReturnHttpStatus201() throws Exception {

        // Given a firestation to add
        final String address = "addAddress";
        final int stationNumber = 89;
        final FirestationRequest addFirestationRequest = new FirestationRequest(address, stationNumber);

        // When the firestation is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isCreated - 201
        response.andExpect(status().isCreated());

        // Then check that Firestation saved
        final List<Firestation> savedStation = repository.getByStation(stationNumber);

        assertThat(savedStation)
                .hasSize(1)
                .extracting("address")
                .contains(address);

    }


    @Test
    void addFirestation_shouldReturnHttpStatus409() throws Exception {

        // Given a firestation to add
        final FirestationRequest addFirestationRequest = new FirestationRequest("834 Binoc Ave", 3);

        // When the firestation is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isConflict - 409
        response.andExpect(status().isConflict());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidFirestationRequest")
    void addFirestation_shouldReturnHttpStatus400(FirestationRequest addFirestationRequest) throws Exception {

        // When the firestation is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
    //          response 200 ---> Firestation successfully updated
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void updateFirestation_shouldReturnHttpStatus200() throws Exception {

        // Given a firestation to update
        final FirestationRequest updateFirestationRequest = new FirestationRequest("112 Steppes Pl", 9);
        final Firestation updatedFirestation = new Firestation("112 Steppes Pl", 9);

        // When the firestation is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that Firestation updated
        assertTrue(repository.getAll().contains(updatedFirestation));
    }


    @Test
    void updateFirestation_shouldReturnHttpStatus404() throws Exception {

        // Given a firestation to update
        final FirestationRequest updateFirestationRequest = new FirestationRequest("unknownAddress", 9);

        // When the firestation is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidFirestationRequest")
    void updateFirestation_shouldReturnHttpStatus400(FirestationRequest updateFirestationRequest) throws Exception {

        // When Firestation posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
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

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that Firestation deleted
        assertFalse(repository.getAll().stream()
                .anyMatch(f -> f.getAddress().equals(address)));

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

        // Then response isNotFound - 404
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

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
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

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that Firestation deleted
        assertFalse(repository.getAll().stream()
                .anyMatch(f -> f.getStation().equals(station_number)));

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

        // Then response isNotFound - 404
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

        // Then response isBadRequest - 400
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