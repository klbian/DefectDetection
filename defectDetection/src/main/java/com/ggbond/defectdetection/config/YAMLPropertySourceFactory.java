package com.ggbond.defectdetection.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Author: 19461
 * Date: 2024/2/5
 */
public class YAMLPropertySourceFactory implements PropertySourceFactory {
    @Override
    public org.springframework.core.env.PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        //创建一个YAML解析工厂。
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        //设置资源。
        factory.setResources(encodedResource.getResource());

        //获取解析后的Properties对象
        Properties properties = factory.getObject();
        //返回。此时不能像默认工厂那样返回ResourcePropertySource对象 ，要返回他的父类PropertiesPropertySource对象。
        assert properties != null;
        if (name != null) {
            return new PropertiesPropertySource(name, properties);
        } else {
            return new PropertiesPropertySource(Objects.requireNonNull(encodedResource.getResource().getFilename()),properties);
        }
    }
}
