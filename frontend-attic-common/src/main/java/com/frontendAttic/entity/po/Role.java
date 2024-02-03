package com.frontendAttic.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.enums.DateTimePatternEnum;
import com.frontendAttic.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统角色表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {


    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    @VerifyParam(required = true, max = 100)
    private String roleName;

    /**
     * 角色描述
     */
    @VerifyParam(max = 300)
    private String roleDesc;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    private List<Integer> menuIds;

    @Override
    public String toString() {
        return "角色ID:" + (roleId == null ? "空" : roleId) + "，角色名称:" + (roleName == null ? "空" : roleName) + "，角色描述:" + (roleDesc == null ? "空" : roleDesc) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，最后更新时间:" + (lastUpdateTime == null ? "空" : DateUtil.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
    }
}
