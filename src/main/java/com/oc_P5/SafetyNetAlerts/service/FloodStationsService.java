package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.MembersByStation;

import java.util.List;

public interface FloodStationsService {

    MembersByStation getMembersByStation(List<Integer> station_Numbers);

}
