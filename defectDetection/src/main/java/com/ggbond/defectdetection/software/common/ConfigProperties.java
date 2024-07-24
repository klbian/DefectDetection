package com.ggbond.defectdetection.software.common;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 配置信息类,由json中加载
 * <p>
 * Author: 19461
 * Date: 2024/2/17
 */
@Data
@Slf4j
@Component("ConfigProperties")
public class ConfigProperties {

    public static ConfigProperties properties=new ConfigProperties();

    private ModelConfig modelConfig;
    private ChartsConfig chartsConfig;
    private WarnsConfig warnsConfig;
    private RunningConfig runningConfig;


    interface Savable{
        void saveConfig();
    }

    @Data
    public static class ModelConfig implements Savable{
        int id;
        String ip;
        int port;
        String resStoragePath;

        @Override
        public void saveConfig() {
            // 读取配置文件内容
            String content;
            try {
                content = new String(Files.readAllBytes(Paths.get("src/main/resources/config.json")));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // 将配置文件内容解析为JSONObject
            JSONObject jsonObject = JSONObject.parseObject(content);

            // 更新对应的配置项
            jsonObject.put("model", this);

            // 将更新后的JSONObject转换为字符串
            String updatedContent = jsonObject.toJSONString();

            // 将更新后的内容写入配置文件
            try {
                Files.write(Paths.get("src/main/resources/config.json"), updatedContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Data
    public static class ChartsConfig implements Savable{
        int granularity;

        @Override
        public void saveConfig() {
            // 读取配置文件内容
            String content;
            try {
                content = new String(Files.readAllBytes(Paths.get("src/main/resources/config.json")));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // 将配置文件内容解析为JSONObject
            JSONObject jsonObject = JSONObject.parseObject(content);

            // 更新对应的配置项
            jsonObject.put("charts", this);

            // 将更新后的JSONObject转换为字符串
            String updatedContent = jsonObject.toJSONString();

            // 将更新后的内容写入配置文件
            try {
                Files.write(Paths.get("src/main/resources/config.json"), updatedContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Data
    public static class WarnsConfig implements Savable{
        double defectiveRateWarningLine;
        int continuousWorkingMinutesWarningLine;
        Long waringInterval;

        @Override
        public void saveConfig() {
            // 读取配置文件内容

            String content;
            try {
                content = new String(Files.readAllBytes(Paths.get("src/main/resources/config.json")));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // 将配置文件内容解析为JSONObject
            JSONObject jsonObject = JSONObject.parseObject(content);

            // 更新对应的配置项
            jsonObject.put("warns", this);

            // 将更新后的JSONObject转换为字符串
            String updatedContent = jsonObject.toJSONString();

            // 将更新后的内容写入配置文件
            try {
                Files.write(Paths.get("src/main/resources/config.json"), updatedContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Data
    public static class RunningConfig implements Savable{
        int detectInterval;

        @Override
        public void saveConfig() {
            // 读取配置文件内容

            String content;
            try {
                content = new String(Files.readAllBytes(Paths.get("src/main/resources/config.json")));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // 将配置文件内容解析为JSONObject
            JSONObject jsonObject = JSONObject.parseObject(content);

            // 更新对应的配置项
            jsonObject.put("running", this);

            // 将更新后的JSONObject转换为字符串
            String updatedContent = jsonObject.toJSONString();

            // 将更新后的内容写入配置文件
            try {
                Files.write(Paths.get("src/main/resources/config.json"), updatedContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostConstruct
    public void loadProperties(){
        String content="";
        try {
            content = new String(Files.readAllBytes(Paths.get("src/main/resources/config.json")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject=JSONObject.parseObject(content);

        //获取模型配置
        String modelConfigStr=jsonObject.getString("model");
        modelConfig=JSONObject.parseObject(modelConfigStr,ModelConfig.class);


        //获取图表配置
        String chartsConfigStr=jsonObject.getString("charts");
        chartsConfig=JSONObject.parseObject(chartsConfigStr,ChartsConfig.class);

        //加载告警配置
        String warnsConfigStr=jsonObject.getString("warns");
        warnsConfig=JSONObject.parseObject(warnsConfigStr,WarnsConfig.class);

        //加载运行配制信息
        String runningCongfigStr=jsonObject.getString("running");
        runningConfig=JSONObject.parseObject(runningCongfigStr,RunningConfig.class);


        //赋值
        properties.modelConfig=this.modelConfig;
        properties.chartsConfig=this.chartsConfig;
        properties.warnsConfig=this.warnsConfig;
        properties.runningConfig=this.runningConfig;
    }

    public void saveProperties(){
        modelConfig.saveConfig();
        chartsConfig.saveConfig();
        warnsConfig.saveConfig();
        runningConfig.saveConfig();
    }

}