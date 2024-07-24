package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理员类
 * <p>
 * Author: 19461
 * Date: 2024/2/15
 */
@Slf4j
@Data
public class Manager {
    @TableId(type = IdType.AUTO)
    Integer id;

    String account;

    @TableField("password")
    String pwd;

    @TableField("phone_number")
    String phone;

    String name;

    String email;

    boolean isOnline;

    boolean warningsOpen;

    int warningsLevel;

    boolean phoneWay;

    boolean emailWay;


    public Manager(){}

    public Manager(Integer id,String account, String phoneNumber, String name, String email) {
        this.id=id;
        this.account = account;
        this.phone= phoneNumber;
        this.name = name;
        this.email = email;
    }
}