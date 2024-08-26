package com.oc_P5.SafetyNetAlerts.service.data_reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Getter
@Slf4j
@Component
public class DataReaderFromJson implements DataReader {

    private DataWrapperList data = new DataWrapperList();

    public DataReaderFromJson(ObjectMapper objectMapper) {
        log.info("SERVICE - DataReaderServiceImpl - Loading datas ... ");

        // Lire le fichier JSON et le convertir en objet DataWrapper
        // objectMapper.readValue lève une exception de type IOException
        try {
            // Todo : use Resource path instead of direct path
            // google : spring boot load file from resources dir
            data = objectMapper.readValue(new File("src/main/resources/data.json"), DataWrapperList.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("SERVICE - data read and sent");
        log.info("data loaded : {} persons, {} stations and  {} medical records", data.getPersons().size() , data.getFireStations().size(), data.getMedicalRecords().size());
    }


}
