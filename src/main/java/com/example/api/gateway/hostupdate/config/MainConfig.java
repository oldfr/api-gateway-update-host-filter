package com.example.api.gateway.hostupdate.config;

import com.example.api.gateway.hostupdate.filter.HostFilter;
import com.example.api.gateway.hostupdate.filter.RequestFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder,
            HostFilter hostFilter, RequestFilter requestFilter) {
        return builder.routes()
                .route("custom-route-1", r -> r.path("/red")
                        .filters(f ->
                            f.rewritePath("/red", "/blue")  // to update uri
                                    .filter(hostFilter.apply(
                                            new HostFilter.Config()))
                                    .filter(requestFilter)
                        )
                        .uri("http://dummyhost.xyz.com")) // the dummy destination host that will be overridden
                .build();
    }
}
