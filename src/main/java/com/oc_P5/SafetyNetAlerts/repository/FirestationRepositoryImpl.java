package com.oc_P5.SafetyNetAlerts.repository;

import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.service.data_reader.DataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FirestationRepositoryImpl implements FirestationRepository {

    private final DataReader dataReaderService;


    @Override
    public List<Firestation> getFirestations() {
        return dataReaderService.getData().getFireStations();
    }

    @Override
    public List<Firestation> getFirestationsByStation(Integer station) {
        return getFirestations()
                .stream()
                .filter(Firestation -> Firestation.getStation().equals(station))
                .toList();
    }
}
