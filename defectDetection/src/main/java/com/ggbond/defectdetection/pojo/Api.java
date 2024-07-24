package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Data
public class Api {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableLogic
    private Integer isDeleted;
    private Date createTime;
    private Integer createId;
    private String createName;

    private LocalDateTime updateTime;
    private Integer updateId;
    private Integer validityPeriod;
    private Integer validityTimes;
    private Integer permissionLevel;
    private String apiKey;
    private Integer status;
    private String remark;
    private Integer counts;

    @TableField(exist = false)
    public int totals =0;

    public Api(){

    }

    public Api(Integer id,Integer validityPeriod, Integer validityTimes, Integer permissionLevel, String remark, Integer status) {
        this.id=id;
        this.validityPeriod = validityPeriod;
        this.validityTimes = validityTimes;
        this.permissionLevel = permissionLevel;
        this.remark = remark;
        this.status = status;
    }

    public Api(Integer validityPeriod, Integer validityTimes, Integer permissionLevel, String apiKey) {
        this.validityPeriod = validityPeriod;
        this.validityTimes = validityTimes;
        this.permissionLevel = permissionLevel;
        this.apiKey = apiKey;
    }
}