package com.frontendAttic.entity.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统角色表参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleQuery extends BaseParam {


	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	private String roleNameFuzzy;

	/**
	 * 角色描述
	 */
	private String roleDesc;

	private String roleDescFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 最后更新时间
	 */
	private String lastUpdateTime;

	private String lastUpdateTimeStart;

	private String lastUpdateTimeEnd;
}