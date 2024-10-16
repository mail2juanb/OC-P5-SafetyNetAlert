package com.oc_P5.SafetyNetAlerts.config;

import com.oc_P5.SafetyNetAlerts.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Enregistre l'intercepteur pour toutes les requêtes
        registry.addInterceptor(requestInterceptor);
    }
}
