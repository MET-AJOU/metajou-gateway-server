package com.minshigee.gatewayserver.wsgateway;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class GatewayWSHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return null;
    }
}