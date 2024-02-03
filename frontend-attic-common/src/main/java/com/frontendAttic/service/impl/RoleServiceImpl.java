package com.frontendAttic.service.impl;

import com.frontendAttic.entity.enums.MenuCheckTypeEnum;
import com.frontendAttic.entity.enums.PageSizeEnum;
import com.frontendAttic.entity.enums.ResponseCodeEnum;
import com.frontendAttic.entity.po.Account;
import com.frontendAttic.entity.po.Role;
import com.frontendAttic.entity.po.RoleMenu;
import com.frontendAttic.entity.query.AccountQuery;
import com.frontendAttic.entity.query.RoleMenuQuery;
import com.frontendAttic.entity.query.RoleQuery;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.mappers.AccountMapper;
import com.frontendAttic.mappers.RoleMapper;
import com.frontendAttic.mappers.RoleMenuMapper;
import com.frontendAttic.service.RoleService;
import com.frontendAttic.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统角色表 业务接口实现
 */
@Service("RoleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private com.frontendAttic.mappers.RoleMapper<Role, RoleQuery> roleMapper;

    @Resource
    private RoleMenuMapper<RoleMenu, RoleMenuQuery> roleMenuMapper;

    @Resource
    private com.frontendAttic.mappers.AccountMapper<Account, AccountQuery> accountMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Role> findListByParam(RoleQuery param) {
        return roleMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(RoleQuery param) {
        return roleMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PageResultVO<Role> findListByPage(RoleQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Role> list = this.findListByParam(param);
        PageResultVO<Role> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Role bean) {
        return roleMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Role> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return roleMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Role> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return roleMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Role bean, RoleQuery param) {
        StringUtil.checkParam(param);
        return roleMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(RoleQuery param) {
        StringUtil.checkParam(param);
        return roleMapper.deleteByParam(param);
    }

    /**
     * 根据RoleId获取对象
     */
    @Override
    public Role getRoleByRoleId(Integer roleId) {
        Role Role = roleMapper.selectByRoleId(roleId);
        List<Integer> selectMenuIds = roleMenuMapper.selectMenuIdsByRoleIds(new String[]{String.valueOf(roleId)});
        Role.setMenuIds(selectMenuIds);
        return Role;
    }

    /**
     * 根据RoleId修改
     */
    @Override
    public Integer updateRoleByRoleId(Role bean, Integer roleId) {
        return roleMapper.updateByRoleId(bean, roleId);
    }

    /**
     * 根据RoleId删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteRoleByRoleId(Integer roleId) {
        AccountQuery query = new AccountQuery();
        query.setRoles(String.valueOf(roleId));
        Integer count = accountMapper.selectCount(query);
        if (count > 0) {
            throw new BusinessException("角色正在被使用无法删除");
        }
        count = roleMapper.deleteByRoleId(roleId);
        RoleMenuQuery roleMenuQuery = new RoleMenuQuery();
        roleMenuQuery.setRoleId(roleId);
        roleMenuMapper.deleteByParam(roleMenuQuery);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(Role Role, String menuIds, String halfMenuIds) {
        Integer roleId = Role.getRoleId();
        Boolean addMenu = false;
        if (null == roleId) {
            Date curDate = new Date();
            Role.setCreateTime(curDate);
            Role.setLastUpdateTime(curDate);
            roleMapper.insert(Role);
            roleId = Role.getRoleId();
            addMenu = true;
        } else {
            Role.setCreateTime(null);
            roleMapper.updateByRoleId(Role, roleId);
        }
        RoleQuery RoleQuery = new RoleQuery();
        RoleQuery.setRoleName(Role.getRoleName());
        Integer roleCount = roleMapper.selectCount(RoleQuery);
        if (roleCount > 1) {
            throw new BusinessException("角色已经存在");
        }
        if (addMenu) {
            saveRoleMenu(roleId, menuIds, halfMenuIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenu(Integer roleId, String menuIds, String halfMenuIds) {
        if (null == roleId || menuIds == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        List<RoleMenu> roleMenuList = new ArrayList<>();
        RoleMenuQuery roleMenuQuery = new RoleMenuQuery();
        roleMenuQuery.setRoleId(roleId);
        roleMenuMapper.deleteByParam(roleMenuQuery);

        String[] menuIdsArray = menuIds.split(",");
        String[] halfMenuIdArray = StringUtil.isEmpty(halfMenuIds) ? new String[]{} : halfMenuIds.split(",");
        convertMenuId2RoleMenu(roleMenuList, roleId, menuIdsArray, MenuCheckTypeEnum.ALL);
        convertMenuId2RoleMenu(roleMenuList, roleId, halfMenuIdArray, MenuCheckTypeEnum.HALF);

        if (!roleMenuList.isEmpty()) {
            roleMenuMapper.insertBatch(roleMenuList);
        }
    }

    private void convertMenuId2RoleMenu(List<RoleMenu> role2MenuList, Integer roleId, String[] menuIdArray,
                                        MenuCheckTypeEnum checkTypeEnum) {
        for (String menuId : menuIdArray) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(Integer.parseInt(menuId));
            roleMenu.setRoleId(roleId);
            roleMenu.setCheckType(checkTypeEnum.getType());
            role2MenuList.add(roleMenu);
        }
    }
}