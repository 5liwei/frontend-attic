package com.frontendAttic.entity.dto;

import java.util.List;

public class DataStatisticsWeekDto {
    private List<String> dateList;
    private List<DataStatisticsDto> itemDataList;

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<DataStatisticsDto> getItemDataList() {
        return itemDataList;
    }

    public void setItemDataList(List<DataStatisticsDto> itemDataList) {
        this.itemDataList = itemDataList;
    }
}