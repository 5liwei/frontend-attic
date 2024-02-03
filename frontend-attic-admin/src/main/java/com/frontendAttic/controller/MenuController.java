package com.frontendAttic.controller;

import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.enums.PermissionCodeEnum;
import com.frontendAttic.entity.po.Menu;
import com.frontendAttic.entity.query.MenuQuery;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单表 Controller
 */
@RestController("MenuController")
@RequestMapping("/settings")
public class MenuController extends BaseController {

    @Resource
    private MenuService menuService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/menuList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_MENU_LIST)
    public ResponseVO menuList() {
        MenuQuery query = new MenuQuery();
        query.setFormate2Tree(true);
        query.setOrderBy("sort asc");
        List<Menu> sysMenuList = menuService.findListByParam(query);
        return createSuccessResponse(sysMenuList);
    }

    @RequestMapping("/saveMenu")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_MENU_MODIFY)
    public ResponseVO saveMenu(@VerifyParam Menu menu) {
        menuService.saveMenu(menu);
        return createSuccessResponse(null);
    }

    @RequestMapping("/delMenu")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_MENU_MODIFY)
    public ResponseVO delMenu(@VerifyParam(required = true) Integer menuId) {
        menuService.deleteMenuByMenuId(menuId);
        return createSuccessResponse(null);
    }
}