package com.example.api.gateway.hostupdate.config;

import com.example.api.gateway.hostupdate.filter.HostFilter;
import com.example.api.gateway.hostupdate.filter.RequestFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Configuration
public class MainConfig {

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder,
            HostFilter hostFilter,
            RequestFilter requestFilter) {
        return builder.routes()
                .route("custom-route-1",
                        r -> r.path("/red")
                                .filters(f ->
                                        f.filter(requestFilter) // to log initial request details
                                                .filter(hostFilter.apply(
                                                        new HostFilter.Config()))
                                                .filter(requestFilter) // to log request details after hostname update
                                )
                                .uri("http://dummyhost.xyz.com")) // the dummy destination host that will be overridden by HostFilter
                .route("custom-route-2",
                        r -> r.path("/green")
                                .and().method(HttpMethod.POST)
                                .and()
                                .readBody(Map.class,s -> true)
                                .filters(f ->
                                        f.filter(requestFilter) // to log initial request details
                                                .filter(hostFilter.apply(
                                                        new HostFilter.Config()))
                                                .filter(requestFilter) // to log request details after hostname update
                                )
                                .uri("http://dummyhost.xyz.com"))
                .build();
    }
}