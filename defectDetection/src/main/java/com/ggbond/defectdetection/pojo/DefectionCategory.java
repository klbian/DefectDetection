package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author: 19461
 * Date: 2024/2/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DefectionCategory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer count;
    private LocalDateTime createTime;
}