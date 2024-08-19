package com.oc_P5.SafetyNetAlerts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.model.DataWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@Slf4j
@SpringBootApplication
public class SafetyNetAlertsApplication {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(SafetyNetAlertsApplication.class, args);

        log.info("Loading datas ... ");

        ObjectMapper objectMapper = new ObjectMapper();

        // Lire le fichier JSON et le convertir en objet DataWrapper
        DataWrapper dataWrapper = objectMapper.readValue(new File("src/main/resources/data.json"), DataWrapper.class);

        log.info("data loaded : {} persons, {} stations and  {} medical records", dataWrapper.getPersons().size() , dataWrapper.getFireStations().size(), dataWrapper.getMedicalRecords().size());

    }

}
