package com.frontendAttic.entity.enums;

public enum AccountStatusEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private Integer status;

    private String desc;

    AccountStatusEnum(Integer status, String desc) {
        this.status = status;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
