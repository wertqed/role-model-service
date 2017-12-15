package ru.rolemodel.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.rolemodel.model.role.RoleEntity;
import ru.rolemodel.model.role.RoleModelService;
import ru.rolemodel.model.user.UserEntity;
import ru.rolemodel.model.user.UserEntityService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by VBelov on 07.12.2017.
 */
@Service
public class UserEntityServiceImpl implements UserEntityService {
    private final String KEY= "USERS";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ListOperations<String, Object> hashOperations;

    @Autowired
    private RoleModelService roleModelService;

    private class Key{
        private String packageName;
        private String idService;
        public Key(String packageName, String idService){
            this.packageName=packageName;
            this.idService=idService;
        }
        @Override
        public String toString(){
            return packageName+":"+idService;
        }
    }
    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForList();
    }

    public String addUsers(List<UserEntity> userEntities) {
        for(UserEntity user: userEntities){
            hashOperations.leftPush(new Key(KEY, user.getIdService()).toString(),  user);
        }
        return "success";
    }

    public String addUser(UserEntity userEntity){
        hashOperations.leftPush(new Key(KEY, userEntity.getIdService()).toString(),  userEntity);
        return "success";
    }

    public List<UserEntity> getUsers(String idService) {
        String key=new Key(KEY, idService).toString();
        List<Object> users= hashOperations.range(key,0, hashOperations.size(key));
        List<UserEntity> userEntities=new ArrayList<UserEntity>();
        for(Object user: users){
            userEntities.add((UserEntity)user);
        }
        return userEntities;
    }

    public List<String> getPermissions(String idService, Integer idUser) {\
        String key=new Key(KEY, idService).toString();
        List<Object> users= hashOperations.range(key,0, hashOperations.size(key));
        List<Object> findUsers=users.stream().filter((userEntity) -> ((UserEntity)userEntity).getId().equals(idUser)).collect(Collectors.toList());
        List<Integer> roles = ((UserEntity)findUsers.get(0)).getRoleEntities();
        List<String> permissions=new ArrayList<>();
        for(RoleEntity role:roleModelService.getRoles(idService)){
            if(roles.contains(role.getId()))
                permissions.addAll(role.getPermissions());
        }
        return permissions;
    }
}
