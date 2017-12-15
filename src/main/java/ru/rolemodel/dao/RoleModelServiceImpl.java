package ru.rolemodel.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.rolemodel.model.role.RoleEntity;
import ru.rolemodel.model.role.RoleModelService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VBelov on 03.12.2017.
 */
@Service
public class RoleModelServiceImpl implements RoleModelService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleModelServiceImpl.class.getName());

    private static final String KEY = "ROLE";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ListOperations<String, Object> hashOperations;

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
        hashOperations = redisTemplate.opsForList();    }

    public String saveRoles(RoleEntity roleEntity) {
        hashOperations.leftPush(KEY, roleEntity.getIdService(), roleEntity);
        return "success";
    }

    public List<RoleEntity> getRoles(String idService) {
        List<Object> roles = hashOperations.range(new Key(KEY, idService).toString(), 0, hashOperations.size(KEY));
        List<RoleEntity> roleEntities=new ArrayList<RoleEntity>();
        for(Object role: roles){
            roleEntities.add((RoleEntity)role);
        }
        return roleEntities;
    }
}