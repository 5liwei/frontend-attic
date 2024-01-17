package com.frontendAttic.entity.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVO<T> {
    private String status;
    private Integer code;
    private String info;
    private T data;
}
