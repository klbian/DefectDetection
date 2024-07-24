package com.ggbond.defectdetection.software.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ggbond.defectdetection.pojo.Device;
import com.ggbond.defectdetection.pojo.SysLog;
import com.ggbond.defectdetection.pojo.WorkOrder;
import com.ggbond.defectdetection.service.DeviceService;
import com.ggbond.defectdetection.service.WorkOrderService;
import com.ggbond.defectdetection.software.data.DataModule;
import com.ggbond.defectdetection.software.face.MainInterface;
import com.ggbond.defectdetection.software.face.RealtimeInterface;
import com.ggbond.defectdetection.software.image.DetectModel;
import com.ggbond.defectdetection.software.image.ImageModule;
import com.ggbond.defectdetection.software.info.Warn;
import com.ggbond.defectdetection.util.SystemStatusUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 软件界面的主类,执行软件的功能
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@Data
@Component
@Slf4j
public class Software {

    //当前工单
    private WorkOrder currentWorkOrder=null;

    //粒度
    private int granularity;

    //工单
    @Autowired
    private WorkOrderService workOrderService;
    //设备
    @Autowired
    private DeviceService deviceService;

    //检测界面
    @Autowired
    MainInterface mainInterface;


    //模块
    //图像检测模块
    @Autowired
    private ImageModule imageModule;

    //数据处理模块
    @Autowired
    private DataModule dataModule;

    //系统信息更新线程
    @Autowired
    SysInfoThread sysInfoThread;

    @Autowired
    Warn warn;

    public Software(){

    }

    //初始化,加载队列,系统的配置文件,历史数据,绘制正确的软件面板,检测与数据库,硬件设备的连接情况
    public void init(){

        //加载粒度
        this.granularity= ConfigProperties.properties.getChartsConfig().getGranularity();

        //工单队列
        LambdaQueryWrapper<WorkOrder> lqw=new LambdaQueryWrapper<>();
        lqw.eq(WorkOrder::getIsOver,0);
        lqw.orderBy(true,true, WorkOrder::getCreateTime);

        List<WorkOrder> workOrderList=workOrderService.list(lqw);
        Queue<WorkOrder> workOrderQueue = new LinkedList<>(workOrderList);

        CommonResource.init(workOrderQueue);

        //加载数据
        if(CommonResource.getCurrentWorkOder()!=null) {
            dataModule.init(CommonResource.getCurrentWorkOder().getId(), granularity);
        } else{
            dataModule.init(null,granularity);
        }
        imageModule.init();

        //加载界面
        //获取设备列表
        List<Device> devices=deviceService.list();
        //获取报表数据
        Map<String,LinkedHashMap> dataMaps=dataModule.getDataMaps();

        this.mainInterface.init(devices,dataMaps);

        //开机为待机状态
        CommonResource.setStatus(SysStatus.NORMAL);

        //系统工具包初始化
        SystemStatusUtil.init();

        this.run();
    }

    //启动软件界面,当软件面板重新打开时可以触发
    public void run(){

        //系统信息面板更新
        sysInfoThread.start();
        //警告监控线程
        new Thread(warn.checkWarningsThread()).start();

    }
}