package ru.rolemodel.model.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.rolemodel.common.CommonResult;

import java.util.List;

/**
 * Created by VBelov on 07.12.2017.
 */
public interface UserEntityService {
    /**
     * Добавляем список пользователей
     * @param userEntities
     * @return
     */
    CommonResult addUsers(List<UserEntity> userEntities);

    CommonResult addUser(UserEntity userEntity);

    CommonResult deleteUser(Integer userId, String idService);

    List<UserEntity> getUsers(String idService);

    CommonResult getPermissions(String idService, Integer idUser);

    CommonResult addUserPermissionSources(UserPermissionSources userPermissionSources);

    Boolean hasPermissionSource(Long userId, String idService, String namePermission, Long idSource);

    CommonResult deleteUserPermissionSources(Long userId, String idService, String namePermission);
}
