package ru.rolemodel.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.rolemodel.common.CommonResult;
import ru.rolemodel.model.role.RoleEntity;
import ru.rolemodel.model.role.RoleModelService;
import ru.rolemodel.model.user.UserEntity;
import ru.rolemodel.model.user.UserEntityService;
import ru.rolemodel.model.user.UserPermissionSources;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by VBelov on 07.12.2017.
 */
@Service
public class UserEntityServiceImpl implements UserEntityService {
    private final String KEY = "USERS";
    private final String KEY_SOURCES = "USERS_SOURCES";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ListOperations<String, Object> hashOperations;

    private ListOperations<String, Object> hashOperationsSources;

    @Autowired
    private RoleModelService roleModelService;

    private class Key {
        private String packageName;
        private String idService;

        public Key(String packageName, String idService) {
            this.packageName = packageName;
            this.idService = idService;
        }

        @Override
        public String toString() {
            return packageName + ":" + idService;
        }
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForList();
        hashOperationsSources = redisTemplate.opsForList();
    }

    public CommonResult addUsers(List<UserEntity> userEntities) {
        for (UserEntity user : userEntities) {
            addUser(user);
        }
        return new CommonResult(true, "Список пользователей успешно добавлен");
    }

    public CommonResult addUser(UserEntity userEntity) {
        String key = new Key(KEY, userEntity.getIdService()).toString();
        List<UserEntity> users = getUsers(userEntity.getIdService());
        List<RoleEntity> roles = roleModelService.getRoles(userEntity.getIdService());
        List<Integer> hasntRoles = new ArrayList<>();
        for (Integer roleId : userEntity.getRoleEntities()) {
            Boolean hasRole=false;
            for (RoleEntity role : roles) {
                if (Objects.equals(role.getId(), roleId)) {
                    hasRole =true;
                    break;
                }
            }
            if(!hasRole){
                hasntRoles.add(roleId);
            }
        }
        if(hasntRoles.size()!=0){
            String temp=" ";
            for(Integer id: hasntRoles){
                temp += id.toString()+ ", ";
            }
            return new CommonResult(false, "Не найдено ролей со следующими id:" + temp);
        }

        for (UserEntity user : users) {
            if (Objects.equals(user.getId(), userEntity.getId())) {
                hashOperations.remove(key, 1, user);
                hashOperations.leftPush(key, userEntity);
                return new CommonResult(true, "Пользователь успешно обновлен");
            }
        }
        hashOperations.leftPush(key, userEntity);
        return new CommonResult(true, "Пользователь успешно добавлен");
    }

    @Override
    public CommonResult deleteUser(Integer userId, String idService) {
        String key = new Key(KEY, idService).toString();
        List<UserEntity> users = getUsers(idService);
        for (UserEntity user : users) {
            if (Objects.equals(user.getId(), userId)) {
                hashOperations.remove(key, 1, user);
                return new CommonResult(true, "Пользователь успешно удален!");
            }
        }
        return new CommonResult(false, "При удалении пользователя произошла ошибка! Пользователь с id = " + userId + " не найден!");
    }

    public List<UserEntity> getUsers(String idService) {
        String key = new Key(KEY, idService).toString();
        List<Object> users = hashOperations.range(key, 0, hashOperations.size(key));
        List<UserEntity> userEntities = new ArrayList<>();
        for (Object user : users) {
            userEntities.add((UserEntity) user);
        }
        return userEntities;
    }

    public CommonResult getPermissions(String idService, Integer idUser) {
        String key = new Key(KEY, idService).toString();
        List<Object> users = hashOperations.range(key, 0, hashOperations.size(key));
        List<Object> findUsers = users.stream().filter((userEntity) -> ((UserEntity) userEntity).getId().equals(idUser)).collect(Collectors.toList());
        if(findUsers.size() == 0){
            return new CommonResult(false, "Не найдено пользователя с id: "+ idUser);
        }
        List<Integer> roles = ((UserEntity) findUsers.get(0)).getRoleEntities();
        List<String> permissions = new ArrayList<>();
        roleModelService.getRoles(idService).stream().filter(role -> roles.contains(role.getId())).forEach(role -> permissions.addAll(role.getPermissions()));
        return new CommonResult(permissions, true);
    }

    @Override
    public CommonResult addUserPermissionSources(UserPermissionSources userPermissionSources) {
        String key = new Key(KEY_SOURCES, userPermissionSources.getIdService()).toString();
        List<Object> permissions = hashOperationsSources.range(key, 0, hashOperations.size(key));
        List<RoleEntity> roles = roleModelService.getRoles(userPermissionSources.getIdService());
        Boolean hasNamePermiss = false;
        for (RoleEntity role : roles) {
            for (String permission : role.getPermissions()) {
                if (Objects.equals(permission, userPermissionSources.getNamePermission())) {
                    hasNamePermiss = true;
                }
            }
        }
        if (!hasNamePermiss) {
            return new CommonResult(false, "Права с именем " + userPermissionSources.getNamePermission() + " не существует");
        }
        for (Object permiss : permissions) {
            if (Objects.equals(((UserPermissionSources) permiss).getUserId(), userPermissionSources.getUserId())
                    && Objects.equals(((UserPermissionSources) permiss).getIdService(), userPermissionSources.getIdService())
                    && Objects.equals(((UserPermissionSources) permiss).getNamePermission(), userPermissionSources.getNamePermission())) {
                hashOperationsSources.remove(key, 1, permiss);
                hashOperationsSources.leftPush(new Key(KEY_SOURCES, userPermissionSources.getIdService()).toString(), userPermissionSources);
                return new CommonResult(true, "Для права успешно обновлены сущности");
            }
        }
        hashOperationsSources.leftPush(new Key(KEY_SOURCES, userPermissionSources.getIdService()).toString(), userPermissionSources);
        return new CommonResult(true, "Для права успешно добавлены сущности");
    }

    @Override
    public Boolean hasPermissionSource(Long userId, String idService, String namePermission, Long idSource) {
        String key = new Key(KEY_SOURCES, idService).toString();
        List<Object> userPermissionSources = hashOperationsSources.range(key, 0, hashOperations.size(key));
        for (Object permis : userPermissionSources) {
            if (((UserPermissionSources) permis).getUserId().equals(userId)
                    && ((UserPermissionSources) permis).getNamePermission().equals(namePermission)
                    && ((UserPermissionSources) permis).getIdSources().contains(idSource)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CommonResult deleteUserPermissionSources(Long userId, String idService, String namePermission) {
        String key = new Key(KEY_SOURCES, idService).toString();
        List<Object> userPermissionSources = hashOperationsSources.range(key, 0, hashOperations.size(key));
        for (Object permiss : userPermissionSources) {
            if (((UserPermissionSources) permiss).getUserId().equals(userId)
                    && ((UserPermissionSources) permiss).getNamePermission().equals(namePermission)) {
                hashOperationsSources.remove(key, 1, permiss);
                return new CommonResult(true, "Успешно удалены сущности для права: " + namePermission);
            }
        }
        return new CommonResult(false, "Права с именем " + namePermission + " не найдено");
    }
}
