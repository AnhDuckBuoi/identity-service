package com.crud.democrud.enums;

public enum Role {
    USER,
    ADMIN;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public String authority() {
        return "ROLE_" + name();
    }
}
