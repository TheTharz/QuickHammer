package com.devnerd.api_gateway.configs;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class SwaggerConfig {

    @Bean
    public CommandLineRunner openApiGroups(
            RouteDefinitionLocator locator,
            SwaggerUiConfigParameters swaggerUiConfigParameters) {
        return args -> {
            List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
            Set<String> processedServices = new HashSet<>();

            if (definitions != null) {
                definitions.forEach(routeDefinition -> {
                    String id = routeDefinition.getId();
                    // Only add service routes (exclude actuator, etc.)
                    if (id.endsWith("-service") && !processedServices.contains(id)) {
                        processedServices.add(id);
                        String name = formatServiceName(id);
                        swaggerUiConfigParameters.addGroup(name);
                    }
                });
            }
        };
    }

    private String formatServiceName(String serviceId) {
        // Convert "auth-service" to "Auth Service"
        String[] parts = serviceId.split("-");
        StringBuilder formatted = new StringBuilder();
        for (String part : parts) {
            if (formatted.length() > 0) {
                formatted.append(" ");
            }
            formatted.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1));
        }
        return formatted.toString();
    }
}
