package ru.rolemodel.dto.roles;

import ru.rolemodel.dto.users.UserDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by VBelov on 25.10.2017.
 */
public class ListUsersDto extends ArrayList<UserDto> implements Serializable {
    private static final long serialVersionUID = 6641786175165593659L;
}
