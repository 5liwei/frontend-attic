package com.frontendAttic.entity.enums;

public enum ImportTemplateEnum {
    QUESTION(0, "/template/template_question.xlsx", "问题模板.xlsx"),
    QUESTION_EXAM(1, "/template/template_exam.xlsx", "试题库模板.xlsx");

    private Integer type;
    private String templatePath;
    private String templateName;

    public Integer getType() {
        return type;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public String getTemplateName() {
        return templateName;
    }

    ImportTemplateEnum(Integer type, String templatePath, String templateName) {
        this.type = type;
        this.templatePath = templatePath;
        this.templateName = templateName;
    }

    public static ImportTemplateEnum getByType(Integer type) {
        for (ImportTemplateEnum item : ImportTemplateEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
