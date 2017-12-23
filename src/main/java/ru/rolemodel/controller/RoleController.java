package ru.rolemodel.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.rolemodel.common.BaseController;
import ru.rolemodel.common.CommonResult;
import ru.rolemodel.dto.roles.ListUsersDto;
import ru.rolemodel.model.role.RoleEntity;
import ru.rolemodel.model.role.RoleModelService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * Created by VBelov on 21.10.2017.
 */
@RestController
@RequestMapping("/roles")
@Api(value = "Role manager")
@EnableSwagger2
public class RoleController extends BaseController {

    @Autowired
    RoleModelService roleModelService;

    @Override
    protected void registerEditors(WebDataBinder binder) {
        binder.registerCustomEditor(ListUsersDto.class, getPropertyEditor(ListUsersDto.class));
//        binder.registerCustomEditor(ListPermissionDto.class, getPropertyEditor(ListPermissionDto.class));
//        binder.registerCustomEditor(RoleUsersDto.class, getPropertyEditor(RoleUsersDto.class));
    }

    @ApiOperation(value = "getRolesOfService")
    @RequestMapping(value = "/get/{idService}", method = RequestMethod.GET)
    public List<RoleEntity> getRoles(@PathVariable(value = "idService") String idService) {
        try {
            return roleModelService.getRoles(idService);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @ApiOperation(value = "addRolePermissions")
    @RequestMapping(value = "/addRolePermissions", method = RequestMethod.POST)
    public CommonResult addRolePermissions(RoleEntity roleEntity) {
        try {
            return roleModelService.saveRoles(roleEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, "Ошибка при добавлении роли!");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public CommonResult deleteRole(@RequestParam(value = "roleId") Integer roleId,
                             @RequestParam(value = "idService") String idService) {
        try {
            return roleModelService.deleteRole(idService, roleId);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult(false, "Ошибка при удалении роли!");
        }
    }

}
