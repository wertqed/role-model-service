package ru.rolemodel.model.role;

import java.io.Serializable;
import java.util.List;

/**
 * Created by VBelov on 23.12.2017.
 */
public class RoleUsers implements Serializable{
    private Integer id;
    private String idService;
    private List<Integer> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }
}
