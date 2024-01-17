package com.frontendAttic.service;

import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.po.Account;
import com.frontendAttic.entity.query.AccountQuery;
import com.frontendAttic.entity.vo.PageResultVO;

import java.util.List;


/**
 * 账号信息 业务接口
 */
public interface AccountService {

    /**
     * 条件查询列表
     */
    List<Account> findListByParam(AccountQuery param);

    /**
     * 条件查询数量
     */
    Integer findCountByParam(AccountQuery param);

    /**
     * 分页查询
     */
    PageResultVO<Account> findListByPage(AccountQuery param);

    /**
     * 新增
     */
    Integer add(Account bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<Account> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<Account> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(Account bean, AccountQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(AccountQuery param);

    /**
     * 根据UserId查询对象
     */
    Account getSysAccountByUserId(Integer userId);


    /**
     * 根据UserId修改
     */
    Integer updateSysAccountByUserId(Account bean, Integer userId);


    /**
     * 根据UserId删除
     */
    Integer deleteSysAccountByUserId(Integer userId);


    /**
     * 根据Phone查询对象
     */
    Account getSysAccountByPhone(String phone);


    /**
     * 根据Phone修改
     */
    Integer updateSysAccountByPhone(Account bean, String phone);


    /**
     * 根据Phone删除
     */
    Integer deleteSysAccountByPhone(String phone);

    UserAdminSessionDto login(String phone, String password);

    void saveSysAccount(Account account);
}