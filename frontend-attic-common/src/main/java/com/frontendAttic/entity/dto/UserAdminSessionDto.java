package com.frontendAttic.entity.dto;

import com.frontendAttic.entity.vo.MenuVO;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 用户session 信息
 * @Author LiWei
 * @Date 2024/1/6 15:15
 * @ClassName
 * @MethodName
 * @Params
 */
public class UserAdminSessionDto implements Serializable {

    private static final long serialVersionUID = 1690149993220674991L;
    
    private Integer userId;
    private String userName;
    private Boolean superAdmin;
    private List<MenuVO> menuList;
    private List<String> permissionCodeList;

    public List<String> getPermissionCodeList() {
        return permissionCodeList;
    }

    public void setPermissionCodeList(List<String> permissionCodeList) {
        this.permissionCodeList = permissionCodeList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public List<MenuVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuVO> menuList) {
        this.menuList = menuList;
    }

}
