package ru.rolemodel.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.rolemodel.model.role.RoleModelService;
import ru.rolemodel.model.user.UserEntity;
import ru.rolemodel.model.user.UserEntityService;
import ru.rolemodel.model.user.UserPermissionSources;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        hashOperationsSources= redisTemplate.opsForList();
    }

    public String addUsers(List<UserEntity> userEntities) {
        for (UserEntity user : userEntities) {
            addUser(user);
        }
        return "success";
    }

    public String addUser(UserEntity userEntity) {
        String key=new Key(KEY, userEntity.getIdService()).toString();
        List<UserEntity> users =getUsers(userEntity.getIdService());
        for(UserEntity user: users){
            if(Objects.equals(user.getId(), userEntity.getId())){
                hashOperations.remove(key, 1, user);
                hashOperations.leftPush(key, userEntity);
                return "success";
            }
        }
        hashOperations.leftPush(key, userEntity);
        return "success";
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

    public List<String> getPermissions(String idService, Integer idUser) {
        String key = new Key(KEY, idService).toString();
        List<Object> users = hashOperations.range(key, 0, hashOperations.size(key));
        List<Object> findUsers = users.stream().filter((userEntity) -> ((UserEntity) userEntity).getId().equals(idUser)).collect(Collectors.toList());
        List<Integer> roles = ((UserEntity) findUsers.get(0)).getRoleEntities();
        List<String> permissions = new ArrayList<>();
        roleModelService.getRoles(idService).stream().filter(role -> roles.contains(role.getId())).forEach(role -> permissions.addAll(role.getPermissions()));
        return permissions;
    }

    @Override
    public String addUserPermissionSources(UserPermissionSources userPermissionSources) {
        String key = new Key(KEY_SOURCES, userPermissionSources.getIdService()).toString();
        List<Object> permissions = hashOperationsSources.range(key, 0, hashOperations.size(key));
        for(Object permiss: permissions){
            if(Objects.equals(((UserPermissionSources) permiss).getUserId(), userPermissionSources.getUserId())
                    && Objects.equals(((UserPermissionSources) permiss).getIdService(), userPermissionSources.getIdService()))
                hashOperationsSources.remove(key, 1, permiss);
            hashOperationsSources.leftPush(new Key(KEY_SOURCES, userPermissionSources.getIdService()).toString(), userPermissionSources);
            return "success";
        }
        hashOperationsSources.leftPush(new Key(KEY_SOURCES, userPermissionSources.getIdService()).toString(), userPermissionSources);
        return "success";
    }

    @Override
    public Boolean hasPermissionSource(Long userId, String idService, String namePermission, Long idSource) {
        String key = new Key(KEY_SOURCES, idService).toString();
        List<Object> userPermissionSources = hashOperationsSources.range(key, 0, hashOperations.size(key));
        for(Object permis: userPermissionSources){
            if(((UserPermissionSources)permis).getUserId().equals(userId)
                    && ((UserPermissionSources)permis).getNamePermission().equals(namePermission)
                    && ((UserPermissionSources)permis).getIdSources().contains(idSource)){
                return true;
            }
        }
        return false;
    }
}
