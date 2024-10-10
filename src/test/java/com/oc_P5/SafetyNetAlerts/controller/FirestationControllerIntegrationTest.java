package com.oc_P5.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.controller.requests.AddFirestationRequest;
import com.oc_P5.SafetyNetAlerts.controller.requests.UpdateFirestationRequest;
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
public class FirestationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void getPersonsByStation_shouldReturnResponseEntity() throws Exception {

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


    @ParameterizedTest
    @MethodSource("provideInvalidStationRequest")
    void getPersonsByStation_shouldThrowException(Integer station_number) throws Exception {

        // Given uriPath
        final String uriPath = "/firestation";

        // When Firestation posted
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }


    @Test
    void addFirestation_shouldReturnResponseEntity() throws Exception {

        // Given a firestation to add
        final AddFirestationRequest addFirestationRequest = new AddFirestationRequest("addAddress", 9);

        final String uriPath = "/firestation";

        // When the firestation is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isCreated());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidAddFirestationRequest")
    void addFirestation_shouldThrowException(AddFirestationRequest addFirestationRequest) throws Exception {

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


    @Test
    void updateFirestation_shouldReturnResponseEntity() throws Exception {

        // Given a firestation to update
        final UpdateFirestationRequest updateFirestationRequest = new UpdateFirestationRequest("1509 Culver St", 9);

        final String uriPath = "/firestation";

        // When the firestation is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isOk());

    }


    @ParameterizedTest
    @MethodSource("provideInvalidUpdateFirestationRequest")
    void updateFirestation_shouldThrowException(UpdateFirestationRequest updateFirestationRequest) throws Exception {

        // Given postAddress
        final String uriPath = "/firestation";

        // When Firestation posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(updateFirestationRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }


    @Test
    void deleteFirestationByAddress_shouldReturnResponseEntity() throws Exception {

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


    @ParameterizedTest
    @MethodSource("provideInvalidAddressRequest")
    void deleteFirestationByAddress_shouldThrowException(String address) throws Exception {

        // Given uriPath
        final String uriPath = "/firestation/address";

        // When Firestation posted
        ResultActions response = mockMvc.perform(delete(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }


    @Test
    void deleteFirestationByStation_shouldReturnResponseEntity() throws Exception {

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


    @ParameterizedTest
    @MethodSource("provideInvalidStationRequest")
    void deleteFirestationByStation_shouldThrowException(Integer station_number) throws Exception {

        // Given uriPath
        final String uriPath = "/firestation/station";

        // When Firestation posted
        ResultActions response = mockMvc.perform(delete(uriPath)
                .param("station_number", String.valueOf(station_number))
                .contentType(MediaType.APPLICATION_JSON));

        // Then the firestation is sent to the service
        response.andExpect(status().isBadRequest());

    }



    // Fournit des valeurs de AddFirestationRequest, y compris null
    static Stream<AddFirestationRequest> provideInvalidAddFirestationRequest() {
        AddFirestationRequest firestation1 = new AddFirestationRequest("", 1);
        AddFirestationRequest firestation2 = new AddFirestationRequest(" ", 1);
        AddFirestationRequest firestation3 = new AddFirestationRequest(null, 1);
        AddFirestationRequest firestation4 = new AddFirestationRequest("address", null);
        AddFirestationRequest firestation5 = new AddFirestationRequest("address", 0);
        AddFirestationRequest firestation6 = new AddFirestationRequest("address", -1);

        return Stream.of(firestation1, firestation2, firestation3, firestation4, firestation5, firestation6);
    }

    // Fournit des valeurs de UpdateFirestationRequest, y compris null
    static Stream<UpdateFirestationRequest> provideInvalidUpdateFirestationRequest() {
        UpdateFirestationRequest firestation1 = new UpdateFirestationRequest("", 1);
        UpdateFirestationRequest firestation2 = new UpdateFirestationRequest(" ", 1);
        UpdateFirestationRequest firestation3 = new UpdateFirestationRequest(null, 1);
        UpdateFirestationRequest firestation4 = new UpdateFirestationRequest("address", null);
        UpdateFirestationRequest firestation5 = new UpdateFirestationRequest("address", 0);
        UpdateFirestationRequest firestation6 = new UpdateFirestationRequest("address", -1);

        return Stream.of(firestation1, firestation2, firestation3, firestation4, firestation5, firestation6);
    }

    // Fournit des valeurs d'address, y compris null
    static Stream<String> provideInvalidAddressRequest() {
        String address1 = "";
        String address2 = " ";
        String address3 = null;

        return Stream.of(address1, address2, address3);
    }

    // Fournit des valeurs de station, y compris null
    static Stream<Integer> provideInvalidStationRequest() {
        Integer station1 = null;
        Integer station2 = -2;
        Integer station3 = 0;

        return Stream.of(station1, station2, station3);
    }


}
