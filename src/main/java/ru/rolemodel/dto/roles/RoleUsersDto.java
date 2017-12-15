package ru.rolemodel.dto.roles;

import java.io.Serializable;

/**
 * Created by VBelov on 21.10.2017.
 */
public class RoleUsersDto implements Serializable{
    private static final long serialVersionUID = -8227600144998000889L;

    private Integer id;
    private String name;
    private ListUsersDto listUsersDto;

    public ListUsersDto getListUsersDto() {
        return listUsersDto;
    }

    public void setListUsersDto(ListUsersDto listUsersDto) {
        this.listUsersDto = listUsersDto;
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
}
