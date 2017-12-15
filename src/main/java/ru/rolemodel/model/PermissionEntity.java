package ru.rolemodel.model;

import java.io.Serializable;

/**
 * Created by VBelov on 25.10.2017.
 */
public class PermissionEntity implements Serializable {
    private static final long serialVersionUID = -8756513340046572434L;

    private Long id;
    private String permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
