package com.oc_P5.SafetyNetAlerts.service;

import com.oc_P5.SafetyNetAlerts.dto.MemberByStation;

import java.util.List;

public interface FloodStationsService {

    List<MemberByStation> getMembersByStation(List<Integer> station_Numbers);

}
