package com.oc_P5.SafetyNetAlerts.controller.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class FirestationRequest {

    @NotBlank(message = "address shouldn't be null or blank")
    String address;

    @NotNull(message = "station shouldn't be null")
    @Positive(message = "stationNumber must be positive")
    Integer stationNumber;
}
