package com.example.gateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

//@Component
@Slf4j
public class CustomRouteDefinitionRepository implements RouteDefinitionLocator {

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return null;
    }
}
