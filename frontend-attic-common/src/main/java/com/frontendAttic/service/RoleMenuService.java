package com.frontendAttic.service;

import com.frontendAttic.entity.po.RoleMenu;
import com.frontendAttic.entity.query.RoleMenuQuery;
import com.frontendAttic.entity.vo.PageResultVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 角色对应的菜单权限表 业务接口
 */
public interface RoleMenuService {

	/**
	 * 根据条件查询列表
	 */
	List<RoleMenu> findListByParam(RoleMenuQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(RoleMenuQuery param);

	/**
	 * 分页查询
	 */
	PageResultVO<RoleMenu> findListByPage(RoleMenuQuery param);

	/**
	 * 新增
	 */
	Integer add(RoleMenu bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<RoleMenu> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<RoleMenu> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(RoleMenu bean,RoleMenuQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(RoleMenuQuery param);

	/**
	 * 根据RoleIdAndMenuId查询对象
	 */
	RoleMenu getRoleMenuByRoleIdAndMenuId(Integer roleId,Integer menuId);


	/**
	 * 根据RoleIdAndMenuId修改
	 */
	Integer updateRoleMenuByRoleIdAndMenuId(RoleMenu bean,Integer roleId,Integer menuId);


	/**
	 * 根据RoleIdAndMenuId删除
	 */
	Integer deleteRoleMenuByRoleIdAndMenuId(Integer roleId,Integer menuId);

}