package com.minshigee.gatewayserver.wsgateway;

import com.minshigee.gatewayserver.exception.ErrorCode;
import com.minshigee.gatewayserver.util.JwtUtils;
import com.minshigee.gatewayserver.wsgateway.entity.AuthInfo;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class HandShakeWSService extends HandshakeWebSocketService {

    private final JwtUtils jwtUtils = JwtUtils.getInstance();

    @Override
    public Mono<Void> handleRequest(ServerWebExchange exchange, WebSocketHandler handler) {
        ServerHttpRequest req = exchange.getRequest();
        try {
            if(jwtUtils.isAppropriateRequestForFilter(req)) {
                String token = jwtUtils.resolveToken(req);
                AuthInfo authInfo = jwtUtils.extractAuthInfoFromToken(token);
                if (!authInfo.getUseable())
                    return Mono.error(ErrorCode.AUTH_TOKEN_ERROR.build());
                return super.handleRequest(exchange, handler);
            }
        }
        catch (Exception e) {}
        return Mono.error(ErrorCode.NOT_FOUND_AUTHINFO.build());
    }
}
