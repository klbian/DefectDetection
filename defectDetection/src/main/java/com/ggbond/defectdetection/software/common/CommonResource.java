package com.ggbond.defectdetection.software.common;

import com.ggbond.defectdetection.pojo.WorkOrder;
import com.ggbond.defectdetection.service.WorkOrderService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 通用类,包含软件界面中不独享且共有的信息
 * <p>
 * Author: 19461
 * Date: 2024/2/21
 */
@Slf4j
@Component
public class CommonResource {

    @Autowired
    @Getter
    private WorkOrderService service;

    @Getter
    private static WorkOrderService workOrderService;

    //当前工单
    @Getter
    private static WorkOrder currentWorkOrder=null;

    //工单队列
    @Getter
    @Setter
    private static Queue<WorkOrder> workOrderQueue=new LinkedList<>();

    //系统状态
    @Getter
    @Setter
    private static SysStatus status=SysStatus.NORMAL;

    //工单信息
    @Getter
    private static int seq=1;
    @Getter
    private static int currentNum=-1;
    @Getter
    private static int detectSum=-1;

    //检测总数
    @Getter
    private static int totalDetectSum;
    @Getter
    private static double totalDefectiveRate;

    @PostConstruct
    public void getService(){
        workOrderService=this.service;
    }

    //当前操作员工号
    @Getter
    private static Integer operatorId=1;

    public static void init( Queue<WorkOrder> workOrderQueue){

        CommonResource.workOrderQueue=workOrderQueue;
        log.info(String.valueOf(workOrderQueue.size()));
        if(!CommonResource.workOrderQueue.isEmpty()) {
            currentWorkOrder = CommonResource.workOrderQueue.element();
            currentNum=currentWorkOrder.getCurrentNum();
            detectSum=currentWorkOrder.getDetectSum();
        }else{
            currentNum=0;
            detectSum=0;
            CommonResource.setStatus(SysStatus.NORMAL);
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(null,"工单队列为空","通知",JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }
    public static void updateWorkOrder(){

        if(currentNum>=detectSum){
            WorkOrder workOrder=workOrderQueue.poll();
            assert workOrder != null;
            workOrder.setCurrentNum(currentNum);
            workOrder.setFinishTime(LocalDateTime.now());
            workOrder.setIsOver(1);
            workOrderService.updateById(workOrder);
            if(!workOrderQueue.isEmpty()){
                CommonResource.setStatus(SysStatus.NORMAL);
                currentWorkOrder=workOrderQueue.element();
                currentNum=currentWorkOrder.getCurrentNum();
                detectSum=currentWorkOrder.getDetectSum();
            }else{
                CommonResource.setStatus(SysStatus.NORMAL);
                currentWorkOrder=null;
                currentNum=0;
                detectSum=0;
            }
        }else{
            currentNum+=1;
            totalDetectSum+=1;
        }
    }

    public static void addNewWorkOrder(WorkOrder workOrder){
        workOrderQueue.add(workOrder);
        if(currentWorkOrder==null){
            currentWorkOrder=workOrderQueue.element();
            currentNum=currentWorkOrder.getCurrentNum();
            detectSum=currentWorkOrder.getDetectSum();
        }
    }

    public static void saveCurrentWorkOrder(){
        currentWorkOrder.setCurrentNum(currentNum);
        currentWorkOrder.setUpdateTime(LocalDateTime.now());
        workOrderService.updateById(currentWorkOrder);
    }

    public static WorkOrder getCurrentWorkOder() {
        return currentWorkOrder;
    }

    public static void setCurrentWorkOderQueue(Queue<WorkOrder> workOrderQueue) {
        CommonResource.workOrderQueue = workOrderQueue;
    }
    public static Queue<WorkOrder> getWorkOrderQueue(){
        return workOrderQueue;
    }

    public static SysStatus getStatus() {
        return status;
    }

    public static void setStatus(SysStatus status) {
        CommonResource.status = status;
    }

    public static int getSeq() {
        return seq;
    }

    public static void setSeq(int seq) {
        CommonResource.seq = seq;
    }

    public static int getCurrentNum() {
        return currentNum;
    }

    public static void setCurrentNum(int currentNum) {
        CommonResource.currentNum = currentNum;
    }

    public static int getDetectSum() {
        return detectSum;
    }

    public static void setDetectSum(int detectSum) {
        CommonResource.detectSum = detectSum;
    }

    public static int getTotalDetect(){
        return totalDetectSum;
    }
}