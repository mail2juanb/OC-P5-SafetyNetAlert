package com.oc_P5.SafetyNetAlerts.controller;

import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.model.Person;
import com.oc_P5.SafetyNetAlerts.service.ChildAlertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChildAlertControllerTest {

    @Mock
    ChildAlertServiceImpl childAlertService;

    @InjectMocks
    ChildAlertController childAlertController;


    private List<ChildrenByAddress> childrenAlertListMock;

    @BeforeEach
    public void setUp() {
        Person person1 = new Person("firstNameTest1", "lastNameTest1", "addressTest1", "cityTest1", 9999, "123-456-7890", "emailTest1");
        Person person2 = new Person("firstNameTest2", "lastNameTest1", "addressTest1", "cityTest1", 9999, "123-456-7891", "emailTest2");

        List<Person> familyMembers = new ArrayList<>();
        familyMembers.add(person2);

        ChildrenByAddress childrenByAddress = new ChildrenByAddress(person1, 12, familyMembers);

        childrenAlertListMock = new ArrayList<>();
        childrenAlertListMock.add(childrenByAddress);
    }

    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getChildByAddress_shouldReturnListOfChildrenByAddress() {
        // Given
        String address = "addressTest1";

        when(childAlertService.getChildByAddress(address)).thenReturn(childrenAlertListMock);

        // When
        List<ChildrenByAddress> result = childAlertController.getChildByAddress(address);

        // Then
        verify(childAlertService, times(1)).getChildByAddress(address);
        assertEquals(childrenAlertListMock, result);
    }

    @Test
    // On va vérifier ici que la méthode du service renvoi une liste vide lorsque l'address n'existe pas
    void getChildByAddress_shouldReturnEmptyListWhenAddressNotExist() {
        // Given
        String address = "unknowAddress";

        // When
        List<ChildrenByAddress> result = childAlertController.getChildByAddress(address);

        // Then
        verify(childAlertService, times(1)).getChildByAddress(address);
        assertEquals(Collections.emptyList(), result);
    }
}
