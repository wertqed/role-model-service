package ru.rolemodel.model.role;

import org.springframework.context.annotation.Role;

import java.io.Serializable;
import java.util.List;

/**
 * Created by VBelov on 21.10.2017.
 */
public class RoleEntity implements Serializable{
    private static final long serialVersionUID = 3796813264238036558L;

    private Long id;
    private String idService;
    private String roleName;
    private List<String> permissions;

    public RoleEntity(Long id, String roleName, List<String> permissions){
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }
    public RoleEntity(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }
}
