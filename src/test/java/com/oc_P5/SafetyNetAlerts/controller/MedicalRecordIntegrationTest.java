package com.oc_P5.SafetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.controller.requests.MedicalRecordRequest;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void addMedicalRecord_shouldReturnResponseEntity() throws Exception {

        // Given a medicalRecord to add
        final LocalDate birthdate = LocalDate.now().minusYears(1).minusMonths(1).minusDays(1);
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest addMedicalRecordRequest = new MedicalRecordRequest("firstName", "lastName", birthdate, medicationList, allergiesList);

        final String uriPath = "/medicalRecord";

        // When the medicalRecord is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then the medicalRecord is sent to the service
        response.andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecordRequest")
    void addMedicalRecord_shouldThrowException(MedicalRecordRequest addMedicalRecordRequest) throws Exception {

        // Given a medicalRecord to add
        final String uriPath = "/firestation";

        // When the medicalRecord is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the medicalRecord is sent to the service
        response.andExpect(status().isBadRequest());

    }

    @Test
    void updateMedicalRecord_shouldReturnResponseEntity() throws Exception {

        // Given a medicalRecord to update
        final LocalDate birthdate = LocalDate.now().minusYears(1).minusMonths(1).minusDays(1);
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest updateMedicalRecordRequest = new MedicalRecordRequest("Felicia", "Boyd", birthdate, medicationList, allergiesList);

        final String uriPath = "/medicalRecord";

        // When the medicalRecord is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the medicalRecord is sent to the service
        response.andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecordRequest")
    void updateMedicalRecord_shouldThrowException(MedicalRecordRequest updateMedicalRecordRequest) throws Exception {

        // Given putAddress
        final String uriPath = "/medicalRecord";

        // When MedicalRecord posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the medicalRecord is sent to the service
        response.andExpect(status().isBadRequest());

    }

    @Test
    void deleteMedicalRecord_shouldReturnResponseEntity() throws Exception {

        // Given a medicalRecord to delete
        final MedicalRecordRequest deleteMedicalRecordRequest = new MedicalRecordRequest("John", "Boyd", null, null, null);

        final String uriPath = "/medicalRecord";

        // When the medicalRecord is deleted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deleteMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the medicalRecord is sent to the service
        response.andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecordRequest")
    void deleteMedicalRecord_shouldThrowException(MedicalRecordRequest deleteMedicalRecordRequest) throws Exception {

        // Given putAddress
        final String uriPath = "/medicalRecord";

        // When MedicalRecord posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deleteMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then the medicalRecord is sent to the service
        response.andExpect(status().isBadRequest());

    }



    // Fournit des valeurs de MedicalRecordRequest, y compris null
    static Stream<MedicalRecordRequest> provideInvalidMedicalRecordRequest() {
        final LocalDate birthdateFutur = LocalDate.now().plusYears(1);
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();

        final MedicalRecordRequest medicalRecordRequest1 = new MedicalRecordRequest("", "", null, null, null);
        final MedicalRecordRequest medicalRecordRequest2 = new MedicalRecordRequest(" ", " ", null, null, null);
        final MedicalRecordRequest medicalRecordRequest3 = new MedicalRecordRequest("firstName", "lastName", birthdateFutur, medicationList, allergiesList);


        return Stream.of(medicalRecordRequest1, medicalRecordRequest2, medicalRecordRequest3);
    }


}
