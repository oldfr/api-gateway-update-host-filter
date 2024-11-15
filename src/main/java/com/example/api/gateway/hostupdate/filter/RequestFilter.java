package com.example.api.gateway.hostupdate.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
public class RequestFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("In Request Filter");
        System.out.println("Original Request URL:"+exchange.getRequest().getURI());
        System.out.println("Target hostname:"+((Route)exchange.getAttributes().get(GATEWAY_ROUTE_ATTR)).getUri());
        return chain.filter(exchange);
    }
}
