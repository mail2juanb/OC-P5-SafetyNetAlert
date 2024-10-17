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
    public List<MemberByStation> getMembersByStation(List<Integer> station_Numbers) {
        // NOTE : Pas de vérification de l'existence de personnes à l'adresse demandée puisque la liste retournée peut être vide.

        // NOTE Récupère la liste des address correspondant à la liste de stationNumbers
        List<String> addressList = firestationRepository.getAll()
                .stream()
                .filter(firestation -> station_Numbers.contains(firestation.getStation()))
                .map(Firestation::getAddress)
                .toList();

        // NOTE Récupère la liste des Person desservies grace à la liste des adresses, cette liste est triée par address et par lastName
        List<Person> personList = personRepository.getByAddresses(addressList)
                .stream()
                .sorted(Comparator.comparing(Person::getAddress)
                        .thenComparing(Person::getLastName))
                .toList();

        // NOTE Récupère la liste des id correspondants
        List<String> idList = personList
                .stream()
                .map(Person::getId)
                .toList();

        // NOTE Récupère la liste de PersonWithMedicalRecord avec la idList
        List<PersonWithMedicalRecord> personWithMedicalRecordList = personRepository.getPersonsWithMedicalRecord(idList);

        // NOTE Mapper la liste dans l'objet MembersByStation
        return personWithMedicalRecordList
                .stream()
                .map(FloodStationsServiceImpl::mapToMemberByStation)
                .toList();
    }



    private static MemberByStation mapToMemberByStation(PersonWithMedicalRecord personMedic) {
        return new MemberByStation(personMedic.person(), personMedic.medicalRecord());
    }

}