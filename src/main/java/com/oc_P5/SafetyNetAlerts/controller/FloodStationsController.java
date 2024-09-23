package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.MembersByStation;
import com.oc_P5.SafetyNetAlerts.service.FloodStationsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class FloodStationsController {

    private final FloodStationsServiceImpl floodStationsService;

    public FloodStationsController(FloodStationsServiceImpl floodStationsService) {
        this.floodStationsService = floodStationsService;
    }

    /**
     * GET http://localhost:8080/flood/stations?stations=<a list of station_numbers>
     * @param station_Numbers List of Integer station_Numbers
     * @return Liste des foyers desservis par la caserne - lastName, phone, age, medications, allergies
     * mise en forme : String lastname, String phone, int age, List<String>medications, List<String>allergies
     */
    @GetMapping("/flood/stations")
    public MembersByStation getMembersByStationController(@RequestParam("station_Numbers") List<Integer> station_Numbers) {
        return floodStationsService.getMembersByStation(station_Numbers);
    }

}
