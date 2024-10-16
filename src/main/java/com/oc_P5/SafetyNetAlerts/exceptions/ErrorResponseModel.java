package com.oc_P5.SafetyNetAlerts.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponseModel {
    private String statusCode;
    private Map<String, Object> headers;
    private String typeMessageCode;
    private String titleMessageCode;
    private String detailMessageCode;
    private Object[] detailMessageArguments;
    private Body body;

    @Data
    @AllArgsConstructor
    public static class Body {
        private String type;
        private String title;
        private int status;
        private String detail;
        private Map<String, String> errorFields;
        private Instant timestamp;
    }
}
