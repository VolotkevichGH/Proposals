package com.example.testjwt.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {

    ROLE_ADMIN, ROLE_USER, ROLE_OPERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
