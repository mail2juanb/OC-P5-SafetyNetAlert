package com.oc_P5.SafetyNetAlerts.service;


import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;

import java.util.List;

public interface ChildAlertService {

    List<ChildrenByAddress> getChildByAddress(String address);

}
