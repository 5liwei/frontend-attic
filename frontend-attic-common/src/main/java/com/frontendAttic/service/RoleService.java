package com.frontendAttic.service;

import com.frontendAttic.entity.po.Role;
import com.frontendAttic.entity.query.RoleQuery;
import com.frontendAttic.entity.vo.PageResultVO;

import java.util.List;

/**
 * 系统角色表 业务接口
 */
public interface RoleService {

    /**
     * 根据条件查询列表
     */
    List<Role> findListByParam(RoleQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(RoleQuery param);

    /**
     * 分页查询
     */
    PageResultVO<Role> findListByPage(RoleQuery param);

    /**
     * 新增
     */
    Integer add(Role bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<Role> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<Role> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(Role bean, RoleQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(RoleQuery param);

    /**
     * 根据RoleId查询对象
     */
    Role getRoleByRoleId(Integer roleId);


    /**
     * 根据RoleId修改
     */
    Integer updateRoleByRoleId(Role bean, Integer roleId);


    /**
     * 根据RoleId删除
     */
    Integer deleteRoleByRoleId(Integer roleId);

    void saveRole(Role Role, String menuIds, String halfMenuIds);

    void saveRoleMenu(Integer roleId, String menuIds, String halfMenuIds);
}