package com.minshigee.gatewayserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MainController {

    @GetMapping
    public Mono<String> getHome() {
        return Mono.just("asd");
    }
}
