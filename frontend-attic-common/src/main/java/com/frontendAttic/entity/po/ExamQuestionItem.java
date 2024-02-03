package com.frontendAttic.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionItem implements Serializable {


    private static final long serialVersionUID = -2630327750617171871L;
    /**
     * 选项ID
     */
    private Integer itemId;

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 标题
     */
    private String title;

    /**
     * 排序
     */
    private Integer sort;

    @Override
    public String toString() {
        return "选项ID:" + (itemId == null ? "空" : itemId) + "，问题ID:" + (questionId == null ? "空" : questionId) + "，标题:" + (title == null ? "空" : title) + "，排序:" + (sort == null ? "空" : sort);
    }
}
