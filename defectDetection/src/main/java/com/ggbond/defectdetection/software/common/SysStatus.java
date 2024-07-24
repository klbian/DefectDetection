package com.ggbond.defectdetection.software.common;

public enum SysStatus {

    NORMAL("待机状态",0),WORKING("工作状态",1), STOPPING("停止运行",2), PAUSING("暂停",3),
    ClOSING("软件界面关闭",4);

    private String name;
    private int value;

    SysStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
    public String getName(){
        return this.name;
    }
}
