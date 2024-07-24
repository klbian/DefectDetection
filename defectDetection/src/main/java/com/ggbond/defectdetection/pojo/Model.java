package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Data
public class Model {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String paramStoragePath;
    private Integer isDeleted;
    private Date createTime;
    private Integer createId;
    private LocalDateTime updateTime;
    private Integer updateId;
    private String remark;
}