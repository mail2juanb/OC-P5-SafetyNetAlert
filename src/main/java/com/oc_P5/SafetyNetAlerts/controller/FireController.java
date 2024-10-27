package com.oc_P5.SafetyNetAlerts.controller;


import com.oc_P5.SafetyNetAlerts.dto.FirePersonsResponse;
import com.oc_P5.SafetyNetAlerts.service.FireServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FireController {

    private final FireServiceImpl fireService;



    /**
     * GET /fire?address
     * This url must return the list of inhabitants living at the given address, as well as the
     * number of the firestation serving it. The list must include name, telephone
     * phone number, age and medical history (medication, dosage and allergies) of each allergies).
     * If no address is found in our file, the application will simply return an empty JSON object.
     *
     * @param address String validated with @NotBlank
     * @return ResponseEntity<FirePersonsResponse> (HttpStatus.OK)
     */

    @Operation(summary = "List of inhabitants living there")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FirePersonsResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @GetMapping("/fire")
    public ResponseEntity<FirePersonsResponse> getFirePersonsByAddress(
            @Valid @RequestParam("address")
            @NotBlank(message = "address cannot be blank")
            String address) {

        FirePersonsResponse firePersonsByAddress = fireService.getFirePersonsByAddress(address);

        return new ResponseEntity<>(firePersonsByAddress, HttpStatus.OK);
    }

}
