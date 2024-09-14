package com.example.api.gateway.hostupdate.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class HostFilter extends AbstractGatewayFilterFactory<HostFilter.Config> {

    String domain = "localhost";//".xyz.com";

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("Applying HostName filter");
            ServerHttpRequest request = exchange.getRequest();
            String incomingHost = request.getURI().getHost();;
            String location = request.getQueryParams().getFirst("locality");
            String updatedHost = incomingHost.replace(domain, location+domain);

            URI updatedUri =
                    UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
                            .scheme("http")
                            .host(updatedHost)
                            .port(8080)
                            .build().toUri();

            ServerWebExchange modifiedExchange = modifyExchange(exchange, updatedUri);
            System.out.println("updatedUri:"+updatedUri);
            System.out.println("updatedHost:"+updatedHost);
            System.out.println("Applying HostName filter completed.. ");
            return chain.filter(modifiedExchange);
        };
    }

    private ServerWebExchange modifyExchange(ServerWebExchange exchange, URI updatedUri ) {
        ServerWebExchange modifiedExchange = exchange.mutate().build();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);

        // create new Route object based on old route and updatedURI
        Route newRoute = Route.async()
                .id(route.getId())
                .uri(updatedUri)
                .order(route.getOrder())
                .asyncPredicate(route.getPredicate()).build();

        newRoute.getPredicate().apply(exchange);

        modifiedExchange.getAttributes().put(GATEWAY_ROUTE_ATTR,newRoute); // This step is mandatory as GATEWAY_ROUTE_ATTR will only be set once when gateway property is set. To update host, you need to update this attribute

        System.out.println("returning modifiedExchange");
        return modifiedExchange;
    }

    public static class Config {

    }
}