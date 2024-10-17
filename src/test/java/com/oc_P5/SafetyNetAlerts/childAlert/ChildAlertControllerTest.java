package com.oc_P5.SafetyNetAlerts.childAlert;

import com.oc_P5.SafetyNetAlerts.controller.ChildAlertController;
import com.oc_P5.SafetyNetAlerts.dto.ChildrenByAddress;
import com.oc_P5.SafetyNetAlerts.service.ChildAlertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChildAlertControllerTest {

    @Mock
    ChildAlertServiceImpl childAlertService;

    @InjectMocks
    ChildAlertController childAlertController;



    @Test
    // On va vérifier ici que la méthode du service est déclenchée ainsi que les arguments envoyés
    void getChildByAddress_shouldReturnListOfChildrenByAddress() {
        // Given a known address
        final String address = "addressTest1";

        // When method is called
        ResponseEntity<List<ChildrenByAddress>> response = childAlertController.getChildByAddress(address);

        // Then address is sent to service and check HttpStatus.OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(childAlertService, times(1)).getChildByAddress(address);

        ArgumentCaptor<String> addressArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(childAlertService).getChildByAddress((addressArgumentCaptor.capture()));
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);
    }

}