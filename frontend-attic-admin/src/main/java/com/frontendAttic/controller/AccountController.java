package com.frontendAttic.controller;

import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.config.AppConfig;
import com.frontendAttic.entity.enums.PermissionCodeEnum;
import com.frontendAttic.entity.enums.ResponseCodeEnum;
import com.frontendAttic.entity.enums.UserStatusEnum;
import com.frontendAttic.entity.enums.VerifyRegexEnum;
import com.frontendAttic.entity.po.Account;
import com.frontendAttic.entity.query.AccountQuery;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.service.AccountService;
import com.frontendAttic.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 账号信息 Controller
 */
@RestController("AccountController")
@RequestMapping("/settings")
public class AccountController extends ABaseController {

    @Resource
    private AccountService AccountService;

    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadAccountList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ACCOUNT_LIST)
    public ResponseVO loadAccountList(AccountQuery query) {
        query.setOrderBy("create_time desc");
        query.setQueryRoles(true);
        return createSuccessResponse(AccountService.findListByPage(query));
    }

    @RequestMapping("/saveAccount")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ACCOUNT_MODIFY)
    public ResponseVO saveAccount(@VerifyParam Account Account) {
        AccountService.saveAccount(Account);
        return createSuccessResponse(null);
    }

    @RequestMapping("/updatePassword")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ACCOUNT_PASSWORD_UPDATE)
    public ResponseVO updatePassword(@VerifyParam Integer userId,
                                     @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password) {
        Account updateInfo = new Account();
        updateInfo.setPassword(StringUtil.encodeByMD5(password));
        AccountService.updateAccountByUserId(updateInfo, userId);
        return createSuccessResponse(null);
    }

    @RequestMapping("/updateStatus")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ACCOUNT_STATUS_TOGGLE)
    public ResponseVO updateStatus(@VerifyParam Integer userId,
                                   @VerifyParam(required = true) Integer status) {
        UserStatusEnum userStatusEnum = UserStatusEnum.getByStatus(status);
        if (userStatusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Account updateInfo = new Account();
        updateInfo.setStatus(status);
        AccountService.updateAccountByUserId(updateInfo, userId);
        return createSuccessResponse(null);
    }

    @RequestMapping("/delAccount")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CONFIG_ACCOUNT_DELETE)
    public ResponseVO delAccount(@VerifyParam Integer userId) {
        Account Account = this.AccountService.getAccountByUserId(userId);
        if (!StringUtil.isEmpty(appConfig.getSuperAdminPhones()) && ArrayUtils.contains(appConfig.getSuperAdminPhones().split(","), Account.getPhone())) {
            throw new BusinessException("系统超级管理员不允许删除");
        }
        AccountService.deleteAccountByUserId(userId);
        return createSuccessResponse(null);
    }
}