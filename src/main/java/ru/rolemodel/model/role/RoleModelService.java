package ru.rolemodel.model.role;

import ru.rolemodel.common.CommonResult;

import java.util.List;

/**
 * Created by VBelov on 03.12.2017.
 */
public interface RoleModelService {
    CommonResult saveRoles(RoleEntity roleEntity);
    List<RoleEntity> getRoles(String isService);
    CommonResult deleteRole(String idService, Integer idRole);
}
