package com.ggbond.defectdetection.software.info;

import lombok.Data;

public enum WarningsEnum {

    info("通知",1),warn("警告",2),error("错误",3);

    public String type;
    public int level;

    WarningsEnum(){}

    WarningsEnum(String type,int level){
        this.type=type;
        this.level=level;
    }
}
