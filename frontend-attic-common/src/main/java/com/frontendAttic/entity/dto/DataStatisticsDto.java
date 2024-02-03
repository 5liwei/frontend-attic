package com.frontendAttic.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStatisticsDto {
    private String statisticsName;
    private Integer count;
    private Integer preCount;
    private List<Integer> listData;
}