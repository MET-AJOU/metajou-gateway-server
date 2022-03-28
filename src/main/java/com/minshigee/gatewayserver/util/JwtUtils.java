package com.minshigee.gatewayserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.project.jjwt.secretkey}")
    private String secret;
    @Value("${spring.project.jjwt.tokenname}")
    private String tokenName;
    @Value("${spring.service.root.domain}")
    private String serviceDomains;

    private Key key;

    @PostConstruct
    public void initialize() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUserCodeFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String resolveToken(ServerHttpRequest request) {
        Assert.notNull(request.getCookies().getFirst(tokenName), "accesstoken Cookie가 비었습니다.");
        return request.getCookies().getFirst(tokenName).getValue();
    }

//    public Authentication getAuthentication(String token) {
//        CustomAuthentication authentication = new CustomAuthentication(
//                getUserCodeFromToken(token),
//                getAllClaimsFromToken(token),
//                token
//        );
//        authentication.setIsAuthenticated(validateToken(token));
//
//        return authentication;
//    }

    public Boolean isAppropriateRequestForFilter(ServerHttpRequest request) {
        if (!request.getCookies().containsKey(tokenName))
            return false;
        String token = request.getCookies().getFirst(tokenName).getValue();
        return validateToken(token);
    }
}