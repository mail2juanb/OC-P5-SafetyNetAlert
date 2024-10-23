package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.service.ChildAlertService;
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
public class ChildAlertController {

    private final ChildAlertService childAlertService;


    /**
     * GET /childAlert?address
     * This url should return a list of children (any individual aged 18 or under)
     * living at this address. The list must include the first and last name of each child
     * age and a list of other household members.
     * If there are no children, this url may return an empty string.
     *
     * @param address address (String validated with @NotBlank)
     * @return ResponseEntity<List<ChildrenByAddress>> (HttpStatus.OK)
     */

    @Operation(summary = "List of children living at the address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ChildAlert list successfully retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChildrenByAddress[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request: missing or incorrect parameters", content = @Content)
    })

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildrenByAddress>> getChildByAddress(
            @Valid @RequestParam("address")
            @NotBlank(message = "address cannot be blank")
            String address) {

         List<ChildrenByAddress> childrenByAddress = childAlertService.getChildByAddress(address);

         return new ResponseEntity<>(childrenByAddress, HttpStatus.OK);
    }

}
