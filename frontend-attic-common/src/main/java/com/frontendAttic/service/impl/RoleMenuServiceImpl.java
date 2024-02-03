package com.frontendAttic.service.impl;

import com.frontendAttic.entity.enums.PageSizeEnum;
import com.frontendAttic.entity.po.RoleMenu;
import com.frontendAttic.entity.query.RoleMenuQuery;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.mappers.RoleMenuMapper;
import com.frontendAttic.service.RoleMenuService;
import com.frontendAttic.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色对应的菜单权限表 业务接口实现
 */
@Service("RoleMenuService")
public class RoleMenuServiceImpl implements RoleMenuService {

	@Resource
	private com.frontendAttic.mappers.RoleMenuMapper<RoleMenu, RoleMenuQuery> roleMenuMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<RoleMenu> findListByParam(RoleMenuQuery param) {
		return roleMenuMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(RoleMenuQuery param) {
		return roleMenuMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PageResultVO<RoleMenu> findListByPage(RoleMenuQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<RoleMenu> list = this.findListByParam(param);
		PageResultVO<RoleMenu> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(RoleMenu bean) {
		return roleMenuMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<RoleMenu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return roleMenuMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<RoleMenu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return roleMenuMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(RoleMenu bean, RoleMenuQuery param) {
		StringUtil.checkParam(param);
		return roleMenuMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(RoleMenuQuery param) {
		StringUtil.checkParam(param);
		return roleMenuMapper.deleteByParam(param);
	}

	/**
	 * 根据RoleIdAndMenuId获取对象
	 */
	@Override
	public RoleMenu getRoleMenuByRoleIdAndMenuId(Integer roleId, Integer menuId) {
		return roleMenuMapper.selectByRoleIdAndMenuId(roleId, menuId);
	}

	/**
	 * 根据RoleIdAndMenuId修改
	 */
	@Override
	public Integer updateRoleMenuByRoleIdAndMenuId(RoleMenu bean, Integer roleId, Integer menuId) {
		return roleMenuMapper.updateByRoleIdAndMenuId(bean, roleId, menuId);
	}

	/**
	 * 根据RoleIdAndMenuId删除
	 */
	@Override
	public Integer deleteRoleMenuByRoleIdAndMenuId(Integer roleId, Integer menuId) {
		return roleMenuMapper.deleteByRoleIdAndMenuId(roleId, menuId);
	}
}