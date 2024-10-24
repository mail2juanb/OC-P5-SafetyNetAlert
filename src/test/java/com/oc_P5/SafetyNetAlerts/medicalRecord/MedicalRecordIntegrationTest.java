package com.oc_P5.SafetyNetAlerts.medicalRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.controller.requests.MedicalRecordRequest;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
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
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Autowired
    private MedicalRecordRepository repository;


    private final String uriPath = "/medicalRecord";


    // NOTE Responses possibilities
    //          response 200 ---> MedicalRecord successfully created
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 409 ---> Conflict: MedicalRecord already exists

    @Test
    void addMedicalRecord_shouldReturnHttpStatus201() throws Exception {

        // Given an unknown medicalRecord to add
        final LocalDate birthdate = LocalDate.now().minusYears(1).minusMonths(1).minusDays(1);
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest addMedicalRecordRequest = new MedicalRecordRequest("firstName", "lastName", birthdate, medicationList, allergiesList);

        // When the medicalRecord is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then response isCreated - 201
        response.andExpect(status().isCreated());

        // Then check that MedicalRecord is saved
        final MedicalRecord expectedMedicalRecord = new MedicalRecord(addMedicalRecordRequest);
        final Optional<MedicalRecord> savedMedicalRecord = repository.findById(expectedMedicalRecord.getId());
        assertThat(savedMedicalRecord)
                .isPresent()
                .satisfies(medicalRecord -> {
                    assertThat(medicalRecord.get().getFirstName()).isEqualTo(expectedMedicalRecord.getFirstName());
                    assertThat(medicalRecord.get().getLastName()).isEqualTo(expectedMedicalRecord.getLastName());
                    assertThat(medicalRecord.get().getBirthdate()).isEqualTo(expectedMedicalRecord.getBirthdate());
                    assertThat(medicalRecord.get().getMedications()).isEqualTo(expectedMedicalRecord.getMedications());
                    assertThat(medicalRecord.get().getAllergies()).isEqualTo(expectedMedicalRecord.getAllergies());
                });

    }

    @Test
    void addMedicalRecord_shouldReturnHttpStatus409() throws Exception {

        // Given a known medicalRecord to add
        final LocalDate birthdate = LocalDate.now().minusYears(1).minusMonths(1).minusDays(1);
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest addMedicalRecordRequest = new MedicalRecordRequest("Jonanathan", "Marrack", birthdate, medicationList, allergiesList);

        // When the medicalRecord is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then response isConflict - 409
        response.andExpect(status().isConflict());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecordRequest")
    void addMedicalRecord_shouldReturnHttpStatus400(MedicalRecordRequest addMedicalRecordRequest) throws Exception {

        // When the medicalRecord is post
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post(uriPath)
                        .content(objectMapper.writeValueAsString(addMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
    //          response 200 ---> MedicalRecord successfully updated
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void updateMedicalRecord_shouldReturnHttpStatus200() throws Exception {

        // Given a known medicalRecord to update
        final LocalDate birthdate = LocalDate.now().minusYears(1).minusMonths(1).minusDays(1);
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest updateMedicalRecordRequest = new MedicalRecordRequest("Felicia", "Boyd", birthdate, medicationList, allergiesList);

        // When the medicalRecord is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that MedicalRecord is updated
        final MedicalRecord expectedMedicalRecord = new MedicalRecord(updateMedicalRecordRequest);
        final Optional<MedicalRecord> updatedMedicalRecord = repository.findById(expectedMedicalRecord.getId());

        assertThat(updatedMedicalRecord)
                .isPresent()
                .satisfies(medicalRecord -> {
                    assertThat(medicalRecord.get().getFirstName()).isEqualTo(expectedMedicalRecord.getFirstName());
                    assertThat(medicalRecord.get().getLastName()).isEqualTo(expectedMedicalRecord.getLastName());
                    assertThat(medicalRecord.get().getBirthdate()).isEqualTo(expectedMedicalRecord.getBirthdate());
                    assertThat(medicalRecord.get().getMedications()).isEqualTo(expectedMedicalRecord.getMedications());
                    assertThat(medicalRecord.get().getAllergies()).isEqualTo(expectedMedicalRecord.getAllergies());
                });

    }

    @Test
    void updateMedicalRecord_shouldReturnHttpStatus404() throws Exception {

        // Given an unknown medicalRecord to update
        final LocalDate birthdate = LocalDate.now().minusYears(1).minusMonths(1).minusDays(1);
        final List<String> medicationList = List.of("medication1 : 100mg", "medication2 : 200mg");
        final List<String> allergiesList = Collections.emptyList();
        final MedicalRecordRequest updateMedicalRecordRequest = new MedicalRecordRequest("unknownFirstName", "unknownLastName", birthdate, medicationList, allergiesList);

        // When the medicalRecord is put
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecordRequest")
    void updateMedicalRecord_shouldReturnHttpStatus400(MedicalRecordRequest updateMedicalRecordRequest) throws Exception {

        // When MedicalRecord posted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put(uriPath)
                        .content(objectMapper.writeValueAsString(updateMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }


    // NOTE Responses possibilities
    //          response 200 ---> Firestation successfully deleted
    //          response 400 ---> Invalid request: missing or incorrect parameters
    //          response 404 ---> Unable to find resources related to the request

    @Test
    void deleteMedicalRecord_shouldReturnHttpStatus200() throws Exception {

        // Given a known medicalRecord to delete
        final MedicalRecordRequest deleteMedicalRecordRequest = new MedicalRecordRequest("John", "Boyd", null, null, null);

        // When the medicalRecord is deleted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deleteMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isOk - 200
        response.andExpect(status().isOk());

        // Then check that MedicalRecord is deleted
        final String deletedId = new MedicalRecord(deleteMedicalRecordRequest).getId();
        assertFalse(repository.getAll().stream()
                .anyMatch(medicalRecord -> medicalRecord.getId().equals(deletedId)));

    }

    @Test
    void deleteMedicalRecord_shouldReturnHttpStatus404() throws Exception {

        // Given an unknown medicalRecord to delete
        final MedicalRecordRequest deleteMedicalRecordRequest = new MedicalRecordRequest("unknownFirstName", "unknownLastName", null, null, null);

        // When the medicalRecord is deleted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deleteMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isNotFound - 404
        response.andExpect(status().isNotFound());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidMedicalRecordRequest")
    void deleteMedicalRecord_shouldReturnHttpStatus400(MedicalRecordRequest deleteMedicalRecordRequest) throws Exception {

        // When MedicalRecord deleted
        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.delete(uriPath)
                        .content(objectMapper.writeValueAsString(deleteMedicalRecordRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // Then response isBadRequest - 400
        response.andExpect(status().isBadRequest());

    }



    // Returns invalid MedicalRecordRequest values
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
