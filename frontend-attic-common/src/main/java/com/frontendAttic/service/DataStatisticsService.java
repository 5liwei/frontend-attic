package com.frontendAttic.service;


import com.frontendAttic.entity.dto.DataStatisticsDto;
import com.frontendAttic.entity.dto.DataStatisticsWeekDto;

import java.util.List;


public interface DataStatisticsService {
    List<DataStatisticsDto> getAllData();

    DataStatisticsWeekDto getAppWeekData();

    DataStatisticsWeekDto getContentWeekData();
}
