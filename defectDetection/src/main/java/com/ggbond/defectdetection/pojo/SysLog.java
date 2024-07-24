package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SysLog {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("time")
    @JsonProperty("time")
    private LocalDateTime opTime;

    @JsonProperty("op")
    private String operation;


    @JsonProperty("label")
    private Integer operator;

    private Integer operatorType;  //1 操作员 0 管理人员 2 api

    private String target;

    @TableField(exist = false)
    private String mainRole;

    @TableField(exist = false)
    private Integer totals=0;

    public void setOperatorType(Integer operatorType){
        if(operatorType==1){
            mainRole="操作员";
        }else if(operatorType==0){
            mainRole="管理人员";
        }else if(operatorType==2){
            mainRole="第三方api";
        }
        this.operatorType=operatorType;
    }
}