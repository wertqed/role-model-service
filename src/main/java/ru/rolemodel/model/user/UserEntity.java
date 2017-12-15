package ru.rolemodel.model.user;

import ru.rolemodel.model.PermissionEntity;
import ru.rolemodel.model.role.RoleEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by VBelov on 21.10.2017.
 */
public class UserEntity implements Serializable{
    private static final long serialVersionUID = -600270482202157569L;
    private Integer id;
    private String idService;
    private String name;
    private List<Integer> roleEntities;

    public UserEntity(Integer id, String idService, String name, List<Integer> roleEntities){
        this.id = id;
        this.name = name;
        this.idService=idService;
        this.roleEntities=roleEntities;
    }
    public UserEntity(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(List<Integer> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }
}
