package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.MemberByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.model.PersonWithMedicalRecord;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FloodStationsServiceImpl implements FloodStationsService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    @Override
    public List<MemberByStation> getMembersByStation(List<Integer> stationNumbers) {
        // NOTE : No check on the existence of station_Numbers, since the returned list may be empty.

        List<String> addressList = firestationRepository.getAll()
                .stream()
                .filter(firestation -> stationNumbers.contains(firestation.getStation()))
                .map(Firestation::getAddress)
                .toList();

        List<Person> personList = personRepository.getByAddresses(addressList)
                .stream()
                .sorted(Comparator.comparing(Person::getAddress)
                        .thenComparing(Person::getLastName))
                .toList();

        List<String> idList = personList
                .stream()
                .map(Person::getId)
                .toList();

        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(idList);

        return personWithMedicalRecordList
                .stream()
                .map(FloodStationsServiceImpl::mapToMemberByStation)
                .toList();
    }



    private static MemberByStation mapToMemberByStation(PersonWithMedicalRecord personMedic) {
        return new MemberByStation(personMedic.person(), personMedic.medicalRecord());
    }

}