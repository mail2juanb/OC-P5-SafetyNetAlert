package com.oc_P5.SafetyNetAlerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class DataReaderService {

    DataWrapperList dataWrapperList = new DataWrapperList();
    ObjectMapper objectMapper = new ObjectMapper();

    public DataWrapperList loadDataWrapperList() {
        log.info("SERVICE - DataReaderService - Loading datas ... ");

        // Lire le fichier JSON et le convertir en objet DataWrapper
        // objectMapper.readValue lève une exception de type IOException
        try {
            dataWrapperList = objectMapper.readValue(new File("src/main/resources/data.json"), DataWrapperList.class);
        } catch (IOException e) {
            log.error("IOException - objectMapper.readValue(new File(\"src/main/resources/data.json\"), DataWrapperList.class);");
        }

        log.info("SERVICE - data read and sent");
        return dataWrapperList;
    }
}
