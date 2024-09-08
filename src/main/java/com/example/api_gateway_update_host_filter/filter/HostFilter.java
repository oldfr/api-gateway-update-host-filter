package com.example.api_gateway_update_host_filter.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

@Component
public class HostFilter extends AbstractGatewayFilterFactory<HostFilter.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String incomingHost = request.getURI().getHost();
            String university = request.getQueryParams().getFirst("university");
            String location = request.getQueryParams().getFirst("location");

            URI updatedUri = URI.create("test.xyz.com");
            ServerHttpRequest updatedReq = request.mutate().uri(updatedUri).build();

            ServerWebExchange modifiedExchange = modifyExchange(updatedUri);
            return chain.filter(modifiedExchange);
        };
    }

    private ServerWebExchange modifyExchange(URI updatedUri) {

        // return modified exchange here
        return null;
    }

    static class Config {

    }
}
