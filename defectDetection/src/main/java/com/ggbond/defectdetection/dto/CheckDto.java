package com.ggbond.defectdetection.dto;

import com.ggbond.defectdetection.pojo.Check;
import lombok.Data;

import java.util.List;

/**
 * Author: 19461
 * Date: 2024/6/9
 */
@Data
public class CheckDto {
    //check列表
    private List<Check> checkList;
    //总页数
    private int totalPages;
}