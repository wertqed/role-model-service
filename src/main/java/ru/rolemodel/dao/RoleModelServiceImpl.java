package ru.rolemodel.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.rolemodel.common.CommonResult;
import ru.rolemodel.model.role.RoleEntity;
import ru.rolemodel.model.role.RoleModelService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    }

    public CommonResult saveRoles(RoleEntity roleEntity) {
        String key = new Key(KEY, roleEntity.getIdService()).toString();
        List<RoleEntity> roles = getRoles(roleEntity.getIdService());
        for (RoleEntity role : roles) {
            if (Objects.equals(role.getId(), roleEntity.getId())) {
                hashOperations.remove(key, 1, role);
                hashOperations.leftPush(key, roleEntity);
                return new CommonResult(true, "Изменена роль");
            }
        }
        hashOperations.leftPush(new Key(KEY, roleEntity.getIdService()).toString(), roleEntity);
        return new CommonResult(true, "Добавлена роль");
    }

    public List<RoleEntity> getRoles(String idService) {
        String key = new Key(KEY, idService).toString();
        List<Object> roles = hashOperations.range(key, 0, hashOperations.size(key));
        List<RoleEntity> roleEntities = new ArrayList<>();
        for (Object role : roles) {
            roleEntities.add((RoleEntity) role);
        }
        return roleEntities;
    }

    @Override
    public CommonResult deleteRole(String idService, Integer idRole) {
        String key = new Key(KEY, idService).toString();
        List<RoleEntity> roles = getRoles(idService);
        for (RoleEntity role : roles) {
            if (Objects.equals(role.getId(), idRole)) {
                hashOperations.remove(key, 1, role);
                return new CommonResult(true, "Удалена роль:" + idRole);
            }
        }
        return new CommonResult(false, "Ошибка при удалении роли! Роли с id =" + idRole + " не существует!");
    }
}
