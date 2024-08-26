package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.PersonsByStation;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Medicalrecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.repository.FirestationRepository;
import com.oc_P5.SafetyNetAlerts.repository.MedicalrecordRepository;
import com.oc_P5.SafetyNetAlerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirestationServiceImpl implements FirestationService{

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;

    /*
    http://localhost:8080/firestation?stationNumber=<station_number>
    Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers
    correspondante. Donc, si le numéro de station = 1, elle doit renvoyer les habitants
    couverts par la station numéro 1.
    La liste doit inclure les informations spécifiques suivantes :
        - prénom
        - nom
        - adresse
        - numéro de téléphone
    De plus, elle doit fournir un décompte du nombre d'adultes et du nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone desservie.
    */
    public PersonsByStation getPersonsByStationService(Integer stationNumber) {


        Set<String> stationAddress = firestationRepository.getFirestationsByStation(stationNumber)
                .stream()
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        List<Person> personsByAddress = personRepository.getPersonsByAddress(stationAddress);

        Integer nbrOfMinor = personsByAddress
                .stream()
                .map(p -> p.getId())
                .map(id -> medicalrecordRepository.findMedicalrecordById(id))
                .filter(optionalMedicalrecord -> optionalMedicalrecord.isPresent())
                .filter(optionalMedicalrecord -> optionalMedicalrecord.get().isMinor())
                .toList()
                .size();

        return new  PersonsByStation(personsByAddress, nbrOfMinor );


//
//        log.info("SERVICE - getPersonsByStation" + " - stationNumber = " + stationNumber);
//
//        // Pour commencer il nous faut une liste de casernes dont le stationNumber correspond.
//
//        log.info("SERVICE - Load Firestation List");
//        DataReaderServiceImpl dataReaderService = new DataReaderServiceImpl();
//        DataWrapperList dataWrapperList = dataReaderService.loadDataWrapperList();
//
//        List<Firestation> firestationList = dataWrapperList.getFireStations();
//        List<Firestation> firestationListSelected = new ArrayList<>();
//
//        for (Firestation firestation : firestationList) {
//            //log.info("firestation = " + firestation.getAddress() + " - Number = " + firestation.getStation());
//            if (Integer.parseInt(firestation.getStation()) == stationNumber) {
//                log.info("firestation sélectionnée = " + firestation.getAddress() + " - Number = " + firestation.getStation());
//                firestationListSelected.add(firestation);
//            }
//        }
//
//        // Maintenant j'ai la liste une liste d'objects Caserne représentant les casernes sélectionnées : List<Firestation> firestationListSelected
//        // On va alors créer une liste des personnes dont l'adresse correspond aux adresses de la liste de casernes sélectionnées
//
//        List<Person> personList = dataWrapperList.getPersons();
//        List<Person> personListSelected = new ArrayList<>();
//
//        for (Firestation firestationSelected : firestationListSelected) {
//            log.info("Adresse de caserne recherchée : " + firestationSelected.getAddress() + "  (numberStation : " + firestationSelected.getStation() + ")");
//
//            for (Person person : personList) {
//                //log.info("person = " + person.getFirstName() + " " + person.getLastName() + " - " + person.getAddress());
//                if (firestationSelected.getAddress().equals(person.getAddress())) {
//                    log.info("personSelected = " + person.getFirstName() + " " + person.getLastName() + " - " + person.getAddress());
//                    personListSelected.add(person);
//                }
//            }
//        }
//
//
//        // Maintenant j'ai la liste des personnes concernées par le numéro de caserne demandé : List<Person> personListSelected
//        // Je peux retourner la liste avec les éléments demandés : prénom, nom, adresse, numéro de téléphone
//
//        Map<String, Object> mapPersonFiltered = new HashMap<>();
//
//        mapPersonFiltered.put("Liste des Personnes couvertes par la caserne n° " + stationNumber, personListSelected.stream().map(p -> {
//            Map<String, String> personInfo = new HashMap<>();
//            personInfo.put("firstName", p.getFirstName());
//            personInfo.put("lastName", p.getLastName());
//            personInfo.put("adress", p.getAddress());
//            personInfo.put("phone", p.getPhone());
//            return personInfo;
//        }).collect(Collectors.toList()));
//
//        return mapPersonFiltered;
///*
//        response.put("persons", persons.stream().map(p -> {
//            Map<String, String> personInfo = new HashMap<>();
//            personInfo.put("firstName", p.getFirstName());
//            personInfo.put("lastName", p.getLastName());
//            personInfo.put("adress", p.getAddress());
//            personInfo.put("phone", p.getPhone());
//            return personInfo;
//        }).collect(Collectors.toList()));
//
//
//
//
//        // Il reste à compter le nombre d'adultes et d'enfants dans cette liste de personnes.
//        // Pour cela il faut observer la date de naissance de chaque personne sélectionnée, présente dans son dossier médical
//
//        List<Medicalrecord> medicalrecordList = dataWrapperList.getMedicalRecords();
//        List<Medicalrecord> medicalrecordListSelected = new ArrayList<>();
//
//        for (Medicalrecord medicalrecord : medicalrecordList) {
//            //log.info("medicalRecord = " + medicalrecord.getFirstName() + " " + medicalrecord.getLastName() + " - " + medicalrecord.getBirthdate());
//            for (Person person : personListSelected) {
//                if (person.getFirstName().equals(medicalrecord.getFirstName()) && person.getLastName().equals(medicalrecord.getLastName())) {
//                    log.info("medicalrecordSlected = " + medicalrecord.getFirstName() + " " + medicalrecord.getLastName() + " - " + medicalrecord.getBirthdate());
//                    medicalrecordListSelected.add(medicalrecord);
//                }
//            }
//        }
//
//        // Il nous faut définir si pour chaque dossiers médicaux sélectionnés, le dossier concerne un adulte ou un enfant ( < 18 ans)
//
//        List<Medicalrecord> medicalrecordSelectedAdult = new ArrayList<>();
//        List<Medicalrecord> medicalrecordSelectedChild = new ArrayList<>();
//
//        CalculateAge calculateAge = new CalculateAge();
//        for (Medicalrecord medicalrecordSelected : medicalrecordListSelected) {
//            Integer medicalrecordAge = calculateAge.calculateAge(medicalrecordSelected.getBirthdate());
//            log.info("Le patient à = " + medicalrecordAge + " ans");
//            if (medicalrecordAge >= 18 ) {
//                log.info("Le patient " + medicalrecordSelected.getFirstName() + " " + medicalrecordSelected.getLastName() + " est un adulte");
//                medicalrecordSelectedAdult.add(medicalrecordSelected);
//            } else if (medicalrecordAge < 18) {
//                log.info("Le patient " + medicalrecordSelected.getFirstName() + " " + medicalrecordSelected.getLastName() + " est un enfant");
//                medicalrecordSelectedChild.add(medicalrecordSelected);
//            }
//        }
//
//        // On compte le nombre d'enfants et d'adultes
//
//        Integer adultCount = medicalrecordSelectedAdult.size();
//        Integer childCount = medicalrecordSelectedChild.size();
//        log.info("Il y a " + childCount + " enfants et " + adultCount + " adultes.");
//
//
//        // Il ne reste plus qu'à mettre en page la réponse complète dans une Map<String, Objet>
//        Map<String, Object> response = new HashMap<>();
//        response.put("Titre", "Blblblblbablablablabl...");
//        response.put("List", personListSelected);
//
//
//
//        response.put("persons", persons.stream().map(p -> {
//            Map<String, String> personInfo = new HashMap<>();
//            personInfo.put("firstName", p.getFirstName());
//            personInfo.put("lastName", p.getLastName());
//            personInfo.put("adress", p.getAddress());
//            personInfo.put("phone", p.getPhone());
//            return personInfo;
//        }).collect(Collectors.toList()));
//
//         */
    }
}
