package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.FirePersonsByAddress;
import com.oc_P5.SafetyNetAlerts.exceptions.NullOrEmptyObjectException;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.NamedModel;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FireServiceImpl implements FireService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final FirestationRepository firestationRepository;

    public FirePersonsByAddress getFirePersonsByAddress(String address) {

        // Vérifier si l'adresse est null ou vide
        if (address == null || address.trim().isEmpty()) {
            throw new NullOrEmptyObjectException("Address cannot be null or empty");
        }

        // Récupère la stationNumber correspondant à l'adresse
        Integer stationNumber = firestationRepository.getAll()
                .stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .map(Firestation::getStation)
                .findFirst()
                .orElseThrow();

        // Récupére la liste des personnes correspondant à l'adresse
        List<Person> personsByAddress = personRepository.getByAddress(address);

        // Récupère la liste de l'id des personnes à l'adresse demandée
        List<String> personIds = personsByAddress
                .stream()
                .map(NamedModel::getId)
                .toList();

        // Récupère les dossiers médicaux des person via leur id
        List<MedicalRecord> medicalRecordByAddress = medicalRecordRepository.getAll()
                .stream()
                .filter(medicalRecord -> personIds.contains(medicalRecord.getId()))
                .toList();

        // Récupère la liste accumulée de Person + MedicalRecord à l'adresse demandée
        List<PersonWithMedicalRecord> personWithMedicalRecord = personsByAddress
                .stream()
                .map(person -> mapToPersonWithMedicalRecord(person, medicalRecordByAddress))
                .toList();

        // Retourne une liste de FirePersonByAddress
        List<FirePersonsByAddress.FirePersonByAddress> firePersonByAdressList = personWithMedicalRecord
                .stream()
                .map(FireServiceImpl::mapToFirePersonByAddress)
                .toList();


        return new FirePersonsByAddress(address, stationNumber, firePersonByAdressList);
    }

    private static FirePersonsByAddress.FirePersonByAddress mapToFirePersonByAddress(PersonWithMedicalRecord person) {
        return new FirePersonsByAddress.FirePersonByAddress(person.person, person.medicalRecord);
    }

    private static PersonWithMedicalRecord mapToPersonWithMedicalRecord(Person person, List<MedicalRecord> medicalRecordByAddress) {
        return medicalRecordByAddress
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