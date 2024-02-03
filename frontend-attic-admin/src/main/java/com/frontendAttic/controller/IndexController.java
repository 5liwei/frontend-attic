package com.frontendAttic.controller;

import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.entity.enums.PermissionCodeEnum;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.service.DataStatisticsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("indexController")
@RequestMapping("/index")
public class IndexController extends ABaseController {

    @Resource
    private com.frontendAttic.service.DataStatisticsService DataStatisticsService;

    @RequestMapping("/getAllData")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.DASHBOARD_MAIN)
    public ResponseVO getAllData() {
        return createSuccessResponse(DataStatisticsService.getAllData());
    }

    @RequestMapping("/getAppWeekData")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.DASHBOARD_MAIN)
    public ResponseVO getAppWeekData() {
        return createSuccessResponse(DataStatisticsService.getAppWeekData());
    }

    @RequestMapping("/getContentWeekData")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.DASHBOARD_MAIN)
    public ResponseVO getContentWeekData() {
        return createSuccessResponse(DataStatisticsService.getContentWeekData());
    }
}