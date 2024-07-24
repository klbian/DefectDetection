package com.ggbond.defectdetection;

import com.ggbond.defectdetection.common.SpringContextUtil;
import com.ggbond.defectdetection.software.common.Software;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.swing.*;

@SpringBootApplication

@Slf4j
public class DefectDetectionApplication {

    public static void main(String[] args) {
        System.setProperty("sun.java2d.noddraw", "true");
        new SpringApplicationBuilder(DefectDetectionApplication.class).headless(false). run(args);
        ApplicationContext context=SpringContextUtil.getApplicationContext();
        Software software=context.getBean(Software.class);
//        Web web=context.getBean(Web.class);
        Environment environment = context.getBean(Environment.class);

        SwingUtilities.invokeLater(software::init);

        log.info("---------------------------------------------------------");
        log.info("Swagger访问路径：http://127.0.0.1:"+environment.getProperty("server.port")+"/swagger-ui/index.html");
        log.info("---------------------------------------------------------");
    }

}
