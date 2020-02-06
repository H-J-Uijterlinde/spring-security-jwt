package com.semafoor.as.security;

public enum Roles {
    ADMIN ("ROLE_ADMIN"),
    USER ("ROLE_USER");

    private String role;

    public String getRole() {
        return role;
    }

    Roles(String role) {
        this.role = role;
    }
}
