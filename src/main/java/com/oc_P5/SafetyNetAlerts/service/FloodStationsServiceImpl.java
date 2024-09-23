package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.MembersByStation;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
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
    private final MedicalRecordRepository medicalRecordRepository;
    private final FirestationRepository firestationRepository;

    public MembersByStation getMembersByStation(List<Integer> station_Numbers) {

        // NOTE Vérifier si l'adress est null ou vide
        if(station_Numbers.isEmpty()) {
            throw new NullOrEmptyObjectException("station_Numbers List can not be empty");
        }

        for (Integer station : station_Numbers) {
            System.out.println("station : " + station);
        }

        // NOTE Récupère la liste des address correspondant à la liste de stationNumbers
        List<String> addressList = firestationRepository.getAll()
                .stream()
                .filter(firestation -> station_Numbers.contains(firestation.getStation()))
                .map(Firestation::getAddress)
                .toList();

        for(String str : addressList) {
            System.out.println("address = " + str);
        }

        // NOTE Récupère la liste des Person desservies grace à la liste des adresses, cette liste est triée par address et par nlastName
        List<Person> personList = personRepository.getByAddresses(addressList)
                .stream()
                .sorted(Comparator.comparing(Person::getAddress)
                        .thenComparing(Person::getLastName))
                .toList();

        for(Person person : personList) {
            System.out.println("person = " + person.getId());
        }

        // NOTE Récupère la liste des id correspondants
        List<String> idList = personList
                .stream()
                .map(Person::getId)
                .toList();

        for(String id : idList) {
            System.out.println("id = " + id);
        }

        // NOTE Récupère la liste des MedicalRecord correspondant aux Person ciblées
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.getAll()
                .stream()
                .filter(medicalRecord -> idList.contains(medicalRecord.getId()))
                .toList();

        for(MedicalRecord medicalRecord : medicalRecordList) {
            System.out.println("medicalRecord = " + medicalRecord.getId());
        }

        // NOTE Mapper les Person avec les MedicalRecord récupérés dans une liste de PersonWithMedicalRecord
        List<PersonWithMedicalRecord> personWithMedicalRecordList = personList
                .stream()
                .map(person -> mapToPersonWithMedicalRecord(person, medicalRecordList))
                .toList();

        for(PersonWithMedicalRecord personMedic : personWithMedicalRecordList) {
            System.out.println("personMedic = " + personMedic.person.getId() + " == " + personMedic.medicalRecord.getId() + " -- address = " + personMedic.person.getAddress());
        }

        // NOTE Mapper la liste dans l'objet MembersByStation
        List<MembersByStation.MemberByStation> memberByStationList = personWithMedicalRecordList
                .stream()
                .map(FloodStationsServiceImpl::mapToMemberByStation)
                .toList();

        return new MembersByStation(memberByStationList);
    }

    private static MembersByStation.MemberByStation mapToMemberByStation(PersonWithMedicalRecord personMedic) {
        return new MembersByStation.MemberByStation(personMedic.person, personMedic.medicalRecord);
    }

    private static PersonWithMedicalRecord mapToPersonWithMedicalRecord(Person person, List<MedicalRecord> medicalRecordList) {
        return medicalRecordList
                .stream()
                .filter(medicalRecord -> medicalRecord.getId().equals(person.getId()))
                .map(medicalRecord -> new PersonWithMedicalRecord(person, medicalRecord))
                .findFirst()
                .orElseThrow();
    }

    record PersonWithMedicalRecord(Person person, MedicalRecord medicalRecord){
        public String getFirstName() {
            return person.getFirstName();
        }
        public String getLastName() {
            return person.getLastName();
        }
    }

}
