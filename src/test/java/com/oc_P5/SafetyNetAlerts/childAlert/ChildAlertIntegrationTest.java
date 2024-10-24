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
    void getChildByAddress_shouldReturnHttpStatus200WithKnownAddress() throws Exception {

        // Given an address
        final String address = "1509 Culver St";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that datas in response are correct
        final String expectedStringList = "[{\"firstName\":\"Tenley\",\"lastName\":\"Boyd\",\"age\":12,\"familyMembers\":[{\"firstName\":\"John\",\"lastName\":\"Boyd\"},{\"firstName\":\"Jacob\",\"lastName\":\"Boyd\"},{\"firstName\":\"Roger\",\"lastName\":\"Boyd\"},{\"firstName\":\"Felicia\",\"lastName\":\"Boyd\"}]},{\"firstName\":\"Roger\",\"lastName\":\"Boyd\",\"age\":7,\"familyMembers\":[{\"firstName\":\"John\",\"lastName\":\"Boyd\"},{\"firstName\":\"Jacob\",\"lastName\":\"Boyd\"},{\"firstName\":\"Tenley\",\"lastName\":\"Boyd\"},{\"firstName\":\"Felicia\",\"lastName\":\"Boyd\"}]}]";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedStringList);

    }

    @Test
    void getChildByAddress_shouldReturnHttpStatus200WithUnknownAddress() throws Exception {

        // Given an address
        final String address = "unknownAddress";

        // When method called
        ResultActions response = mockMvc.perform(get(uriPath)
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that datas in response are correct
        final String expectedStringList = "[]";
        assertThat(response.andReturn().getResponse().getContentAsString()).isEqualTo(expectedStringList);

    }

    @ParameterizedTest
    @MethodSource("provideInvalidAddressRequest")
    void getChildByAddress_shouldReturnHttpStatus400(String address) throws Exception {

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