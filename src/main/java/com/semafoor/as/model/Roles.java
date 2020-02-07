package com.semafoor.as.model;

/**
 * Enum providing predefined roles. When more fine grained authorities within a certain role are desired, consider
 * making this an entity, in which each role has a collection of authorities.
 */

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
