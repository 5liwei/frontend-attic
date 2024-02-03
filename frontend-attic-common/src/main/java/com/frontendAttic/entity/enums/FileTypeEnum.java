package com.frontendAttic.entity.enums;

public enum FileTypeEnum {
    CATEGORY(0, 150, "分类图片"),
    Carousel(1, 400, "轮播图"),
    SHARE_LARGE(2, 400, "分享大图"),
    SHARE_SMALL(3, 100, "分享小图");

    private Integer type;
    private Integer maxWidth;
    private String description;

    FileTypeEnum(Integer type, Integer maxWidth, String description) {
        this.type = type;
        this.maxWidth = maxWidth;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public static FileTypeEnum getType(Integer type) {
        for (FileTypeEnum at : FileTypeEnum.values()) {
            if (at.type.equals(type)) {
                return at;
            }
        }
        return null;
    }
}
