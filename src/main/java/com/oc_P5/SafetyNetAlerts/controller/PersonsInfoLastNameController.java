package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.PersonInfoLastName;
import com.oc_P5.SafetyNetAlerts.exceptions.NotFoundException;
import com.oc_P5.SafetyNetAlerts.service.PersonsInfoLastNameServiceImpl;
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
public class PersonsInfoLastNameController {

    private final PersonsInfoLastNameServiceImpl personsInfoLastNameService;


    /**
     * GET http://localhost:8080/personInfoLastName=<lastName>
     * Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail et les antécédents
     * médicaux (médicaments, posologie et allergies) de chaque habitant. Si plusieurs
     * personnes portent le même nom, elles doivent toutes apparaître.
     * @param lastName String lastName
     * @return Liste des personnes portant le lastName demandé.
     * @throws NotFoundException si le lastName est introuvable
     */
    @GetMapping("/personInfoLastName")
    public ResponseEntity<List<PersonInfoLastName>> getPersonsInfoLastName(
            @Valid @RequestParam("lastName")
            @NotBlank(message = "lastName cannot be blank")
            String lastName) {

        List<PersonInfoLastName> personInfoLastName = personsInfoLastNameService.getPersonsInfoLastName(lastName);

        return new ResponseEntity<>(personInfoLastName, HttpStatus.OK);
    }

}
