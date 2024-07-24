package com.ggbond.defectdetection.software.face;

import com.ggbond.defectdetection.config.YAMLPropertySourceFactory;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 配置属性类,加载配置属性
 * <p>
 * Author: 19461
 * Date: 2024/2/5
 */
@Data
@Component
@ToString
@PropertySource(value="classpath:attributes.yaml",factory = YAMLPropertySourceFactory.class)
public class GUIAttributes {

    @Value("${maininterface.test}")
    public boolean test;

    @Value("${maininterface.size.width}")
    public int width;

    @Value("${maininterface.size.height}")
    public int height;

    @Value("${maininterface.location.x}")
    public int x;

    @Value("${maininterface.location.y}")
    public int y;

    @Value("${maininterface.title}")
    public String title;

    @Value("${leftinterface.name}")
    public String leftName;

    @Value("${middleinterface.name}")
    public String middleName;

    @Value("${rightinterface.name}")
    public String rightName;

    @Value("${middleinterface.charts-save-path}")
    public String chartsSavePath;

    public static GUIAttributes attributes;

    @PostConstruct
    public void setAttributes(){
        attributes=this;
        attributes.height=this.height;
        attributes.width=this.width;
        attributes.x=this.x;
        attributes.y=this.y;
        attributes.title=this.title;
        attributes.leftName=this.leftName;
        attributes.middleName=this.middleName;
        attributes.rightName=this.rightName;
        attributes.chartsSavePath=this.chartsSavePath;
    }


}