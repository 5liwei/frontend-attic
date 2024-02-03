package com.frontendAttic.controller;

import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.enums.VerifyRegexEnum;
import com.frontendAttic.entity.po.Account;
import com.frontendAttic.entity.po.CreateCodeImage;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.entity.constants.Constants;
import com.frontendAttic.service.AccountService;
import com.frontendAttic.utils.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class LoginController extends BaseController {

    @Resource
    private AccountService accountService;

    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {
        CreateCodeImage vCode = new CreateCodeImage(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY, code);
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/login")
    @GlobalInterceptor(checkLogin = false)
    public ResponseVO login(HttpSession session,
                            @VerifyParam(regex = VerifyRegexEnum.PHONE) String phone,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode) {
        if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
            throw new BusinessException("图片验证码错误");
        }
        UserAdminSessionDto userAdminDto = accountService.login(phone, password);
        session.setAttribute(Constants.SESSION_KEY, userAdminDto);
        return createSuccessResponse(userAdminDto);
    }

    @RequestMapping("/logout")
    @GlobalInterceptor(checkLogin = false)
    public ResponseVO logout(HttpSession session) {
        session.invalidate();
        return createSuccessResponse(null);
    }

    @RequestMapping("/updateMyPwd")
    @GlobalInterceptor
    public ResponseVO updateMyPwd(HttpSession session,
                                  @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password) {
        UserAdminSessionDto userAdminDto = getUserAdminFromSession(session);
        Account Account = new Account();
        Account.setPassword(StringUtil.encodeByMD5(password));
        accountService.updateAccountByUserId(Account, userAdminDto.getUserId());
        return createSuccessResponse(null);
    }
}
