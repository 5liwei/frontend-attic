package com.frontendAttic.service.impl;

import com.frontendAttic.entity.config.AppConfig;
import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.enums.MenuTypeEnum;
import com.frontendAttic.entity.enums.PageSizeEnum;
import com.frontendAttic.entity.enums.AccountStatusEnum;
import com.frontendAttic.entity.enums.UserStatusEnum;
import com.frontendAttic.entity.po.Account;
import com.frontendAttic.entity.po.Menu;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.query.AccountQuery;
import com.frontendAttic.entity.query.MenuQuery;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.entity.vo.MenuVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.mappers.AccountMapper;
import com.frontendAttic.service.AccountService;
import com.frontendAttic.service.MenuService;
import com.frontendAttic.utils.CopyUtil;
import com.frontendAttic.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 账号信息 业务接口实现
 */
@Service("AccountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper<Account, AccountQuery> accountMapper;

    @Resource
    private MenuService MenuService;

    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Account> findListByParam(AccountQuery param) {
        return this.accountMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AccountQuery param) {
        return this.accountMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PageResultVO<Account> findListByPage(AccountQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Account> list = this.findListByParam(param);
        PageResultVO<Account> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Account bean) {
        return this.accountMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Account> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.accountMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Account> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.accountMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Account bean, AccountQuery param) {
        StringUtil.checkParam(param);
        return this.accountMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AccountQuery param) {
        StringUtil.checkParam(param);
        return this.accountMapper.deleteByParam(param);
    }

    /**
     * 根据UserId获取对象
     */
    @Override
    public Account getAccountByUserId(Integer userId) {
        return this.accountMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId修改
     */
    @Override
    public Integer updateAccountByUserId(Account bean, Integer userId) {
        return this.accountMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除
     */
    @Override
    public Integer deleteAccountByUserId(Integer userId) {
        return this.accountMapper.deleteByUserId(userId);
    }

    /**
     * 根据Phone获取对象
     */
    @Override
    public Account getAccountByPhone(String phone) {
        return this.accountMapper.selectByPhone(phone);
    }

    /**
     * 根据Phone修改
     */
    @Override
    public Integer updateAccountByPhone(Account bean, String phone) {
        return this.accountMapper.updateByPhone(bean, phone);
    }

    /**
     * 根据Phone删除
     */
    @Override
    public Integer deleteAccountByPhone(String phone) {
        return this.accountMapper.deleteByPhone(phone);
    }

    @Override
    public UserAdminSessionDto login(String phone, String password) {
        Account account = this.accountMapper.selectByPhone(phone);
        System.out.println(account);
        if (account == null) {
            throw new BusinessException("账号或者密码错误");
        }

        if (AccountStatusEnum.DISABLE.getStatus().equals(account.getStatus())) {
            throw new BusinessException("账号已禁用");
        }
        if (!account.getPassword().equals(password)) {
            throw new BusinessException("账号或者密码错误");
        }

        UserAdminSessionDto adminDto = new UserAdminSessionDto();
        adminDto.setUserId(account.getUserId());
        adminDto.setUserName(account.getUserName());

        List<Menu> allMenus;
        if (!StringUtil.isEmpty(appConfig.getSuperAdminPhones()) &&
                ArrayUtils.contains(appConfig.getSuperAdminPhones().split(","), phone)) {
            adminDto.setSuperAdmin(true);

            MenuQuery query = new MenuQuery();
            query.setFormate2Tree(false);
            query.setOrderBy("sort asc");
            allMenus = MenuService.findListByParam(query);
        } else {
            adminDto.setSuperAdmin(false);
            allMenus = MenuService.getAllMenuByRoleIds(account.getRoles());
        }
        List<String> permissionCodeList = new ArrayList<>();

        List<Menu> menuList = new ArrayList<>();
        for (Menu Menu : allMenus) {
            if (MenuTypeEnum.MEMU.getType().equals(Menu.getMenuType())) {
                menuList.add(Menu);
            }
            permissionCodeList.add(Menu.getPermissionCode());
        }
        menuList = MenuService.convertLine2Tree4Menu(menuList, 0);
        if (menuList.isEmpty()) {
            throw new BusinessException("请联系管理员分配角色");
        }
        List<MenuVO> menuVOList = new ArrayList<>();
        menuList.forEach(item -> {
            MenuVO menuVO = CopyUtil.copy(item, MenuVO.class);
            menuVO.setChildren(CopyUtil.copyList(item.getChildren(), MenuVO.class));
            menuVOList.add(menuVO);
        });
        adminDto.setMenuList(menuVOList);
        adminDto.setPermissionCodeList(permissionCodeList);
        return adminDto;
    }

    @Override
    public void saveAccount(Account account) {
        Account phoneDb = accountMapper.selectByPhone(account.getPhone());
        if (phoneDb != null && (account.getUserId() == null || !phoneDb.getUserId().equals(account.getUserId()))) {
            throw new BusinessException("手机号已存在");
        }
        if (account.getUserId() == null) {
            account.setCreateTime(new Date());
            account.setStatus(UserStatusEnum.ENABLE.getStatus());
            account.setPassword(StringUtil.encodeByMD5(account.getPassword()));
            this.accountMapper.insert(account);
        } else {
            account.setPassword(null);
            account.setStatus(null);
            this.accountMapper.updateByUserId(account, account.getUserId());
        }
    }
}