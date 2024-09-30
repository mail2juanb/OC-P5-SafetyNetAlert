package com.oc_P5.SafetyNetAlerts.service;

import java.util.List;

public interface CommunityEmailService {

    List<String> getCommunityEmailByCity(String city);
}
