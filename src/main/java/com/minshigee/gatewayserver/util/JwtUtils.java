package com.minshigee.gatewayserver.util;

import com.minshigee.gatewayserver.exception.ErrorCode;
import com.minshigee.gatewayserver.wsgateway.entity.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtUtils {

    private static class Singleton{
        private static final JwtUtils instance = new JwtUtils();
    }
    public static JwtUtils getInstance (){
        return Singleton.instance;
    }
    private JwtUtils() {}

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

    public AuthInfo extractAuthInfoFromToken(String token) {
        Claims data = getAllClaimsFromToken(token);
        try {
            AuthInfo authInfo = AuthInfo.builder()
                .userCode(getUserCodeFromToken(token))
                .userEmail(data.get("email", String.class))
                .roles(data.get("role", List.class))
                .build();
            return authInfo;
        }
        catch (Exception e) {}
        throw ErrorCode.AUTH_TOKEN_ERROR.build();
    }

    public Boolean isAppropriateRequestForFilter(ServerHttpRequest request) {
        if (!request.getCookies().containsKey(tokenName))
            return false;
        String token = request.getCookies().getFirst(tokenName).getValue();
        return validateToken(token);
    }

    public Boolean isAppropriateRequestForFilter(MultiValueMap<String,HttpCookie> cookies) {
        if (!cookies.containsKey(tokenName))
            return false;
        String token = resolveToken(cookies);
        return validateToken(token);
    }

    public String resolveToken(ServerHttpRequest request) {
        if(request.getCookies().getFirst(tokenName) == null)
            throw ErrorCode.NOT_FOUND_AUTHINFO.build();
        return request.getCookies().getFirst(tokenName).getValue();
    }

    public String resolveToken(MultiValueMap<String,HttpCookie> cookies) {
        if(cookies.getFirst(tokenName) == null)
            throw ErrorCode.NOT_FOUND_AUTHINFO.build();
        return cookies.getFirst(tokenName).getValue();
    }
}