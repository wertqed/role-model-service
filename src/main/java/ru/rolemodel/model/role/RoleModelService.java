package ru.rolemodel.model.role;

import java.util.List;

/**
 * Created by VBelov on 03.12.2017.
 */
public interface RoleModelService {
    String saveRoles(RoleEntity roleEntity);
    List<RoleEntity> getRoles(String isService);
}
