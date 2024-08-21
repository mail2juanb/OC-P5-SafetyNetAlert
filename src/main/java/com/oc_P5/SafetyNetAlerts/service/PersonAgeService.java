package com.oc_P5.SafetyNetAlerts.service;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class PersonAgeService {

    // On va définir ici, dans un Map<String, Object>, le nombre d'adultes et d'enfants à partir d'une liste de personnes.
    public void countChildAndAdult(Map<String, Object> personMap) {
        log.info("SERVICE - countChildAndAdult - Map<String, Object>");
        log.info("Contenu de personMap: " + personMap);

        for (Map.Entry<String, Object> entry : personMap.entrySet()) {
            String strKey = entry.getKey();
            Object objValue = entry.getValue();
            log.info(new StringBuilder().append(strKey).append(" : ").append(objValue).toString());
        }

    }
}
