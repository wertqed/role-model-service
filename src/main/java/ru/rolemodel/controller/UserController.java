package ru.rolemodel.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.rolemodel.common.BaseController;
import ru.rolemodel.common.CommonResult;
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
    public CommonResult getPermissions(@PathVariable(value="idService") String idService,@PathVariable(value="idUser") Integer idUser){
        return userEntityService.getPermissions(idService, idUser);
    }

    @ApiOperation(value = "addUsers")
    @RequestMapping(value= "/addUsers", method = RequestMethod.POST)
    public CommonResult addUsersRoles(List<UserEntity> userEntities){
        try {
            return userEntityService.addUsers(userEntities);
        }catch (Exception e){
            return new CommonResult(false, "При добавлении списка пользователей произошла ошибка!");
        }

    }

    @ApiOperation(value = "addUser")
    @RequestMapping(value= "/addUser", method = RequestMethod.POST)
    public CommonResult addUserRoles(UserEntity userEntity){
        try {
            return userEntityService.addUser(userEntity);
        }catch (Exception e){
            return new CommonResult(false,"При добавлении пользователя произошла ошибка!");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public CommonResult deleteUser(@RequestParam(value = "userId") Integer userId,
                             @RequestParam(value = "idService") String idService) {
        try {
            return userEntityService.deleteUser(userId, idService);
        }catch (Exception e){
            return new CommonResult(false, "При удалении пользователя "+userId+ " произошла ошибка");
        }
    }

    @RequestMapping(value= "/addUserPermissionSources", method = RequestMethod.POST)
    public CommonResult addUserPermissionSources(UserPermissionSources userPermissionSources){
        try {
            return userEntityService.addUserPermissionSources(userPermissionSources);
        }catch (Exception e){
            return new CommonResult(false, "При добавлении сущностей права произошла ошибка!");
        }
    }

    @ApiOperation(value = "hasPermissionSource")
    @RequestMapping(value = "/hasPermissionSource", method = RequestMethod.GET)
    public Boolean hasPermissionSource(@RequestParam(value = "userId") Long userId,
                                  @RequestParam(value = "idService") String idService,
                                  @RequestParam(value = "namePermission") String namePermission,
                                  @RequestParam(value = "idSource") Long idSource) {
        return userEntityService.hasPermissionSource(userId, idService, namePermission, idSource);
    }

    @RequestMapping(value = "/deleteUserPermissionSources", method = RequestMethod.DELETE)
    public CommonResult deleteUserPermissionSources(@RequestParam(value = "userId") Long userId,
                                              @RequestParam(value = "idService") String idService,
                                              @RequestParam(value = "namePermission") String namePermission){
        try {
            return userEntityService.deleteUserPermissionSources(userId, idService, namePermission);
        }catch (Exception e){
            return new CommonResult(false, "При удалении сущности права произошла ошибка");
        }
    }
}
