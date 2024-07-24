package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class Operator {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer jobId;
    private String loginPwd;
    private String opPwd;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private Integer createId;
    private LocalDateTime updateTime;
    private Integer updateId;
    private String createName;
    private String name;
    private Integer workOrderId;
    private String remark;

    @TableField(exist = false)
    public int totals =0;

    public Operator(){}
    public Operator(Integer jobId, String loginPwd, String opPwd, String name, String remark) {
        this.jobId = jobId;
        this.loginPwd = loginPwd;
        this.opPwd = opPwd;
        this.name = name;
        this.remark = remark;
    }
}