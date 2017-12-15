package ru.rolemodel.dto.roles;

import ru.rolemodel.model.PermissionEntity;

import java.io.Serializable;
import java.security.Permission;
import java.util.ArrayList;

/**
 * Created by VBelov on 25.10.2017.
 */
public class ListPermissionDto extends ArrayList<PermissionEntity> implements Serializable{
    private static final long serialVersionUID = 7731225830900542374L;
}
