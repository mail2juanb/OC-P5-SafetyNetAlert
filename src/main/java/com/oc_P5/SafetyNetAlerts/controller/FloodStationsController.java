package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.MemberByStation;
import com.oc_P5.SafetyNetAlerts.service.FloodStationsServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FloodStationsController {

    private final FloodStationsServiceImpl floodStationsService;


    /**
     * GET http://localhost:8080/flood/stations?stations=List<station_numbers>
     * Cette url doit retourner une liste de tous les foyers desservis par la caserne. Cette
     * liste doit regrouper les personnes par adresse. Elle doit aussi inclure le nom, le
     * numéro de téléphone et l'âge des habitants, et faire figurer leurs antécédents
     * médicaux (médicaments, posologie et allergies) à côté de chaque nom.
     * @param station_Numbers List of Integer station_Numbers
     * @return Liste des foyers desservis par la caserne - lastName, phone, age, medications, allergies
     * Mise en forme : String lastname, String phone, int age, List<String>medications, List<String>allergies
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<List<MemberByStation>> getMembersByStation(
            @Valid @RequestParam("station_Numbers")
            @NotNull(message = "station_Numbers cannot be null")
            @Size(min = 1, message = "station_Numbers cannot be empty")
            List<@NotNull(message = "station_Number cannot be null")
                @Positive(message = "station_Number must be positive")
                Integer> station_Numbers) {

        List<MemberByStation> memberByStationList = floodStationsService.getMembersByStation(station_Numbers);

        return new ResponseEntity<>(memberByStationList, HttpStatus.OK);
    }

}