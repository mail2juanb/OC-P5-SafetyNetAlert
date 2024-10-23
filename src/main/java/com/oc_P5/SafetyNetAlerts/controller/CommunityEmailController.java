package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.service.CommunityEmailServiceImpl;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunityEmailController {

    private final CommunityEmailServiceImpl communityEmailService;

    /**
     * GET /communityEmail?city
     * This url should return the e-mail addresses of all the town's inhabitants.
     *
     * @param city name of city validated with String city @NotBlank
     * @return ResponseEntity<List<String>> (HttpStatus.OK)
     * @throws NotFoundException if town cannot be found
     */

    @Operation(summary = "E-mail addresses of all the town's inhabitants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "E-mail list successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unable to find resources related to the request", content = @Content)
    })

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getCommunityEmailByCity(
            @Valid @RequestParam("city")
            @NotBlank(message = "city can't be blank")
            String city) {

        List<String> communityEmail = communityEmailService.getCommunityEmailByCity(city);

        return new ResponseEntity<>(communityEmail, HttpStatus.OK);
    }

}
