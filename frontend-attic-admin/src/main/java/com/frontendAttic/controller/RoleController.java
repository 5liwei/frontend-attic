package com.frontendAttic.controller;

import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.enums.PermissionCodeEnum;
import com.frontendAttic.entity.po.Role;
import com.frontendAttic.entity.query.RoleQuery;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统角色表 Controller
 */
@RestController("RoleController")
@RequestMapping("/settings")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadRoles")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ROLE_LIST)
    public ResponseVO loadRoles(RoleQuery query) {
        query.setOrderBy("create_time desc");
        return createSuccessResponse(roleService.findListByPage(query));
    }

    @RequestMapping("/loadAllRoles")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ROLE_LIST)
    public ResponseVO loadAllRoles() {
        RoleQuery query = new RoleQuery();
        query.setOrderBy("create_time desc");
        return createSuccessResponse(roleService.findListByParam(query));
    }

    /**
     * 新增
     */
    @RequestMapping("/saveRole")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ROLE_MODIFY)
    public ResponseVO saveRole(@VerifyParam Role bean,
                               String menuIds,
                               String halfMenuIds) {
        roleService.saveRole(bean, menuIds, halfMenuIds);
        return createSuccessResponse(null);
    }

    @RequestMapping("/getRoleByRoleId")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ROLE_LIST)
    public ResponseVO getRoleByRoleId(@VerifyParam(required = true) Integer roleId) {
        Role Role = roleService.getRoleByRoleId(roleId);
        return createSuccessResponse(Role);
    }


    @RequestMapping("/saveRoleMenu")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ROLE_MODIFY)
    public ResponseVO saveRoleMenu(@VerifyParam(required = true) Integer roleId,
                                   @VerifyParam(required = true) String menuIds,
                                   String halfMenuIds) {
        roleService.saveRoleMenu(roleId, menuIds, halfMenuIds);
        return createSuccessResponse(null);
    }

    @RequestMapping("/delRole")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ROLE_DELETE)
    public ResponseVO delRole(@VerifyParam(required = true) Integer roleId) {
        roleService.deleteRoleByRoleId(roleId);
        return createSuccessResponse(null);
    }
}