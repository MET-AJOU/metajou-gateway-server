package com.minshigee.gatewayserver.websocket;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@NoArgsConstructor
public class GatewayHandshakeWSService extends HandshakeWebSocketService {

    @Override
    public Mono<Void> handleRequest(ServerWebExchange exchange, WebSocketHandler handler) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            if (true){

                return super.handleRequest(exchange, handler);
            }
        }
        catch (Exception e) {}
        // If not valid, return error
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"));
    }
}
