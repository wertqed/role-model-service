package ru.rolemodel.model.user;

import com.fasterxml.jackson.core.JsonProcessingException;

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
    String addUsers(List<UserEntity> userEntities);

    String addUser(UserEntity userEntity);

    List<UserEntity> getUsers(String idService);

    List<String> getPermissions(String idService, Integer idUser);

    String addUserPermissionSources(UserPermissionSources userPermissionSources);

    Boolean hasPermissionSource(Long userId, String idService, String namePermission, Long idSource);
}
