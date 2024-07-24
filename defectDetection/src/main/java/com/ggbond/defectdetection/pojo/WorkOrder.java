package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Data
@ToString
public class WorkOrder {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private Integer createId;
    private Integer currentNum;
    private Integer detectSum;
    private Integer isOver;
    private LocalDateTime finishTime;
    private LocalDateTime updateTime;
    private Integer updateId;
    private String remark;
}