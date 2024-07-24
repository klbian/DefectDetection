package com.ggbond.defectdetection.software.info;

public enum OpEnum {
    Add("添加"),Update("更新"),Delete("删除"),
    Login("登录"),
    Start("开始"),Stop("停止"),Create("创建"),Pause("暂停"),
    Get("获取"),Manipulate("操控");

    private final String name;

    OpEnum(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }
}
