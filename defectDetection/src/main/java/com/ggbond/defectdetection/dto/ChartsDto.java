package com.ggbond.defectdetection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 表格信息dto
 * <p>
 * Author: 19461
 * Date: 2024/2/23
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ChartsDto<T> {
    String name;
    String type;
    int index;
    T source;
}