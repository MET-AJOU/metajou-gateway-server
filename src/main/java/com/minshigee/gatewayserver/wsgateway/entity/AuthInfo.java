package com.minshigee.gatewayserver.wsgateway.entity;

import lombok.Builder;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.List;

@Builder
public class AuthInfo implements Principal {
    private final String userCode;
    private final String userEmail;
    private final List<Role> roles;

    public boolean getUseable() {
        if (userCode == null)
            return false;
        if (userEmail == null)
            return false;
        if (roles == null)
            return false;
        return true;
    }

    @Override
    public String getName() {
        return userCode;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
