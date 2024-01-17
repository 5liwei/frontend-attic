package com.frontendAttic.entity.enums;

/**
 * 菜单权限编码
 */
public enum PermissionCodeEnum {
    PERMISSION_NONE("permission_none", "不校验权限"),

    DASHBOARD_MAIN("dashboard_main", "首页"),

    CONFIG_SYSTEM("config_system", "系统设置"),
    CONFIG_MENU_LIST("config_menu_list", "菜单列表"),
    CONFIG_MENU_MODIFY("config_menu_modify", "新增/修改/删除"),

    CONFIG_ROLE_LIST("config_role_list", "角色列表"),
    CONFIG_ROLE_MODIFY("config_role_modify", "新增/修改/删除"),
    CONFIG_ROLE_DELETE("config_role_delete", "删除"),

    CONFIG_ACCOUNT_LIST("config_account_list", "用户列表"),
    CONFIG_ACCOUNT_MODIFY("config_account_modify", "新增/修改"),
    CONFIG_ACCOUNT_DELETE("config_account_delete", "删除"),
    CONFIG_ACCOUNT_PASSWORD_UPDATE("config_account_password_update", "修改密码"),
    CONFIG_ACCOUNT_STATUS_TOGGLE("config_account_status_toggle", "启用/禁用"),

    MANAGE_CONTENT("manage_content", "内容管理"),

    LIST_CATEGORY("list_category", "分类列表"),
    EDIT_CATEGORY("edit_category", "新增/修改/删除"),
    DELETE_CATEGORY("delete_category", "删除"),

    LIST_QUESTION("list_question", "问题列表"),
    EDIT_QUESTION("edit_question", "新增/修改"),
    IMPORT_QUESTION("import_question", "导入"),
    POST_QUESTION("post_question", "发布/取消发布"),
    DELETE_QUESTION("delete_question", "删除"),
    BATCH_DELETE_QUESTION("batch_delete_question", "批量删除"),

    LIST_EXAM_QUESTION("list_exam_question", "考试列表"),
    EDIT_EXAM_QUESTION("edit_exam_question", "新增/修改"),
    IMPORT_EXAM_QUESTION("import_exam_question", "导入"),
    POST_EXAM_QUESTION("post_exam_question", "发布/取消发布"),
    DELETE_EXAM_QUESTION("delete_exam_question", "删除"),
    BATCH_DELETE_EXAM_QUESTION("batch_delete_exam_question", "批量删除"),

    LIST_SHARE("list_share", "经验分享列表"),
    EDIT_SHARE("edit_share", "新增/修改"),
    POST_SHARE("post_share", "发布/取消发布"),
    DELETE_SHARE("delete_share", "删除"),
    BATCH_DELETE_SHARE("batch_delete_share", "删除"),

    LIST_APP_UPDATE("list_app_update", "应用发布列表"),
    EDIT_APP_UPDATE("edit_app_update", "应用发布新增/修改/删除"),
    POST_APP_UPDATE("post_app_update", "应用发布"),

    LIST_APP_CAROUSEL("list_app_carousel", "轮播图列表"),
    EDIT_APP_CAROUSEL("edit_app_carousel", "轮播图/修改/删除"),

    LIST_APP_FEEDBACK("list_app_feedback", "问题反馈"),
    REPLY_APP_FEEDBACK("reply_app_feedback", "回复问题反馈"),

    LIST_APP_USER("list_app_user", "App用户"),
    EDIT_APP_USER("edit_app_user", "App用户编辑"),

    LIST_APP_DEVICE("list_app_device", "App用户设备");

    private String code;
    private String desc;

    PermissionCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
