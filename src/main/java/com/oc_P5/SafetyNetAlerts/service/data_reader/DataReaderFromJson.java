package com.oc_P5.SafetyNetAlerts.service.data_reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Slf4j
@Component
public class DataReaderFromJson implements DataReader {

    private DataWrapperList data = new DataWrapperList();

    public DataReaderFromJson(ObjectMapper objectMapper, ResourceLoader resourceLoader) {

        log.info("SERVICE - DataReaderServiceImpl - Loading datas ... ");

        Resource resource = resourceLoader.getResource("classpath:data.json");

        try {
            data = objectMapper.readValue(resource.getInputStream(), DataWrapperList.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("SERVICE - data read and sent");
        log.info("data loaded : {} persons, {} stations and  {} medical records", data.getPersons().size() , data.getFireStations().size(), data.getMedicalRecords().size());
    }


}
