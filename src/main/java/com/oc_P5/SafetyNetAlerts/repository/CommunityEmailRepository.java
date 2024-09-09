package com.oc_P5.SafetyNetAlerts.repository;

import java.util.List;

public interface CommunityEmailRepository {

    List<String> getCommunityEmailByCity(String city);
}
