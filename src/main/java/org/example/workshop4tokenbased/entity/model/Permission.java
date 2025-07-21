package org.example.workshop4tokenbased.entity.model;

public enum Permission {
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_VIP_READ("developers:vip:read"),
    DEVELOPERS_WRITE("developers:write"),;

    private final String permission;

    public String getPermission() {
        return permission;
    }

    Permission(String permission) {
        this.permission = permission;
    }
}