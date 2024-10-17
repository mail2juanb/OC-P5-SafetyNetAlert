package com.oc_P5.SafetyNetAlerts.childAlert;

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
public class ChildAlertIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String uriPath = "/childAlert";



    // NOTE Responses possibilities
    //          response 200 ---> ChildAlert list successfully retrieved
    //          response 400 ---> Invalid request: missing or incorrect parameters

    @Test
    void getChildByAddress_shouldReturnHttpStatus200() throws Exception {

        // Given an address
        final String address = "address";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then the address is sent to the service and status isOk
        response.andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidAddressRequest")
    void getChildByAddress_shouldReturnHttpStatus400(String address) throws Exception {

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then the address is sent to the service and status isBadRequest
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