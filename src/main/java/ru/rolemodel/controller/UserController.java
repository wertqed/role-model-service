package ru.rolemodel.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.rolemodel.common.BaseController;
import ru.rolemodel.dto.roles.ListUsersDto;
import ru.rolemodel.model.user.UserEntity;
import ru.rolemodel.model.user.UserEntityService;
import ru.rolemodel.model.user.UserPermissionSources;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VBelov on 21.10.2017.
 */
@RestController
@RequestMapping("/users")
@Api(value = "UserEntity manager")
@EnableSwagger2
public class UserController extends BaseController {

    @Autowired
    UserEntityService userEntityService;

    @Override
    protected void registerEditors(WebDataBinder binder) {
        binder.registerCustomEditor(ListUsersDto.class, getPropertyEditor(ListUsersDto.class));
    }

    @ApiOperation(value = "getUsers")
    @RequestMapping(value = "/getUsers/{idService}", method = RequestMethod.GET)
    public List<UserEntity> getUsers(@PathVariable(value="idService") String idService){
        return userEntityService.getUsers(idService);
    }

    @ApiOperation(value = "getPermissions")
    @RequestMapping(value = "/getPermissions/{idService}/{idUser}", method = RequestMethod.GET)
    public List<String> getPermissions(@PathVariable(value="idService") String idService,@PathVariable(value="idUser") Integer idUser){
        return userEntityService.getPermissions(idService, idUser);
    }

    @ApiOperation(value = "addUsers")
    @RequestMapping(value= "/addUsers", method = RequestMethod.POST)
    public String addUsersRoles(List<UserEntity> userEntities){
        return userEntityService.addUsers(userEntities);

    }

    @ApiOperation(value = "addUser")
    @RequestMapping(value= "/addUser", method = RequestMethod.POST)
    public String addUserRoles(UserEntity userEntity){
        return userEntityService.addUser(userEntity);
    }

    @RequestMapping(value= "/addUserPermissionSources", method = RequestMethod.POST)
    public String addUserPermissionSources(UserPermissionSources userPermissionSources){
        return userEntityService.addUserPermissionSources(userPermissionSources);
    }

    @ApiOperation(value = "hasPermissionSource")
    @RequestMapping(value = "/hasPermissionSource", method = RequestMethod.GET)
    public Boolean hasPermissionSource(@RequestParam(value = "userId") Long userId,
                                  @RequestParam(value = "idService") String idService,
                                  @RequestParam(value = "namePermission") String namePermission,
                                  @RequestParam(value = "idSource") Long idSource) {
        return userEntityService.hasPermissionSource(userId, idService, namePermission, idSource);
    }
}
