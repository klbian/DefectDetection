package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Defection {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Double l;  //长
    private Double h; //高
    private Double x; //x坐标
    private Double y; //y 坐标
    private Double score;
    private Integer detectId;
    private String category;
    private Integer categoryId;
}