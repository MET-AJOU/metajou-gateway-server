package com.minshigee.gatewayserver.wsgateway;

import com.minshigee.gatewayserver.exception.ErrorCode;
import com.minshigee.gatewayserver.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GatewayWSHandler implements WebSocketHandler {

    JwtUtils jwtUtils = JwtUtils.getInstance();

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        if(jwtUtils.isAppropriateRequestForFilter(webSocketSession.getHandshakeInfo().getCookies()))
            return Mono.error(ErrorCode.AUTH_TOKEN_ERROR.build()).and(webSocketSession.close());
        String token = jwtUtils.resolveToken(webSocketSession.getHandshakeInfo().getCookies());
        String userCode = jwtUtils.getUserCodeFromToken(token);

        Flux<WebSocketMessage> stringFlux = webSocketSession.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(String::toUpperCase)
                .map(webSocketSession::textMessage);
        return webSocketSession.send(stringFlux);
    }
}
