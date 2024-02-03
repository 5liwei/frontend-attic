package com.frontendAttic.entity.query;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色对应的菜单权限表参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuQuery extends BaseParam {


	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 菜单ID
	 */
	private Integer menuId;

	/**
	 * 0:半选 1:全选
	 */
	private Integer checkType;

}
