package com.ggbond.defectdetection.pojo;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.ggbond.defectdetection.service.DeviceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Device {


    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer type;
    private String mac;
    private String ip;
    private String name;
    private String remark;

    @TableLogic
    private Integer isDeleted;
    private LocalDateTime createTime;
    private Integer createId;
    private LocalDateTime updateTime;
    private Integer updateId;

    private Boolean isConnect=false;

    public static boolean testConnect(Device device){
        return true;
    }

    public static void allDeviceOffline(DeviceService deviceService){
        Device device=new Device();
        device.setIsConnect(false);
        deviceService.update(device,null);
    }
}