package com.ggbond.defectdetection.dto;

import com.ggbond.defectdetection.pojo.SysLog;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: 19461
 * Date: 2024/2/23
 */
@Data
@AllArgsConstructor
public class DashboardInfoDto {
    private int runTime;
    private int defectionsSum;
    private double defectRate;
    private String highestOccurrenceDefect;
    private SysLog LatestOperations;
}