package ru.rolemodel.dto.users;

import java.io.Serializable;

/**
 * Created by VBelov on 25.10.2017.
 */
public class UserDto implements Serializable {
    private static final long serialVersionUID = -2818056716952997340L;

    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
