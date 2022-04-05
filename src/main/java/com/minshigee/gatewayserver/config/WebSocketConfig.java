package com.minshigee.gatewayserver.config;

import com.minshigee.gatewayserver.wsgateway.GatewayWSHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig{

    @Autowired
    private GatewayWSHandler handler;

    @Bean
    public HandlerMapping handlerMapping(){
        Map<String, GatewayWSHandler> handlerMap = Map.of(
                "/gateway", handler
        );
        return new SimpleUrlHandlerMapping(handlerMap, 1);
    }

}
