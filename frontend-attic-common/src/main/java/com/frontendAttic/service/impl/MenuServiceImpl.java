package com.frontendAttic.service.impl;

import com.frontendAttic.entity.enums.PageSizeEnum;
import com.frontendAttic.entity.po.Menu;
import com.frontendAttic.entity.query.MenuQuery;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.mappers.MenuMapper;
import com.frontendAttic.service.MenuService;
import com.frontendAttic.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 菜单表 业务接口实现
 */
@Service("MenuService")
public class MenuServiceImpl implements MenuService {

    public static final Integer DEFUALT_ROOT_MENUID = 0;

    private static final String ROOT_MENU_NAME = "所有菜单";

    @Resource
    private com.frontendAttic.mappers.MenuMapper<Menu, MenuQuery> MenuMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Menu> findListByParam(MenuQuery param) {
        List<Menu> MenuList = this.MenuMapper.selectList(param);
        if (param.getFormate2Tree() != null && param.getFormate2Tree()) {
            Menu root = new Menu();
            root.setMenuId(DEFUALT_ROOT_MENUID);
            root.setPId(-1);
            root.setMenuName(ROOT_MENU_NAME);
            MenuList.add(root);
            MenuList = convertLine2Tree4Menu(MenuList, -1);
        }
        return MenuList;
    }

    @Override
    public List<Menu> convertLine2Tree4Menu(List<Menu> dataList, Integer pid) {
        List<Menu> children = new ArrayList<>();
        for (Menu m : dataList) {
            if (m.getMenuId() != null && m.getPId() != null && m.getPId().equals(pid)) {
                m.setChildren(convertLine2Tree4Menu(dataList, m.getMenuId()));
                children.add(m);
            }
        }
        return children;
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(MenuQuery param) {
        return this.MenuMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PageResultVO<Menu> findListByPage(MenuQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Menu> list = this.findListByParam(param);
        PageResultVO<Menu> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Menu bean) {
        return this.MenuMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Menu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.MenuMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Menu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.MenuMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Menu bean, MenuQuery param) {
        StringUtil.checkParam(param);
        return this.MenuMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(MenuQuery param) {
        StringUtil.checkParam(param);
        return this.MenuMapper.deleteByParam(param);
    }

    /**
     * 根据MenuId获取对象
     */
    @Override
    public Menu getMenuByMenuId(Integer menuId) {
        return this.MenuMapper.selectByMenuId(menuId);
    }

    /**
     * 根据MenuId修改
     */
    @Override
    public Integer updateMenuByMenuId(Menu bean, Integer menuId) {
        return this.MenuMapper.updateByMenuId(bean, menuId);
    }

    /**
     * 根据MenuId删除
     */
    @Override
    public Integer deleteMenuByMenuId(Integer menuId) {
        return this.MenuMapper.deleteByMenuId(menuId);
    }

    @Override
    public void saveMenu(Menu Menu) {
        if (Menu.getMenuId() == null) {
            this.MenuMapper.insert(Menu);
        } else {
            this.MenuMapper.updateByMenuId(Menu, Menu.getMenuId());
        }
    }

    @Override
    public List<Menu> getAllMenuByRoleIds(String roleIds) {
        if (StringUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        int[] roleIdArray = Arrays.stream(roleIds.split(",")).mapToInt(Integer::valueOf).toArray();
        return MenuMapper.selectAllMenuByRoleIds(roleIdArray);
    }
}