package com.ggbond.defectdetection.software.info;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.pojo.*;
import com.ggbond.defectdetection.service.ApiService;
import com.ggbond.defectdetection.service.SysLogService;
import com.ggbond.defectdetection.software.common.CommonResource;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.software.common.SysStatus;
import com.ggbond.defectdetection.software.face.MainInterface;
import com.ggbond.defectdetection.util.SseUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于日志的处理,包括监听操作,记录变化,输出日志信息
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@Data
@Aspect
@Component
@Slf4j
public class Log {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ApiService apiService;

    private static SysLogService logService;

    private static MainInterface mainInterface;

    private static SseUtil sseUtil;

    @Getter
    private static Map<Class,String> classToName=new HashMap<>();

    @Getter
    private static Map<Integer,String> typeToName=new HashMap<>();



    public Log(){

    }

    //定义切点
    @Pointcut("@annotation(LogPoint)")
    public void logPointAnnotation(){}

    @Pointcut("execution(* com.ggbond.defectdetection.software.face.common.OpPanel.*Button(..))")
    public void logPointMethod(){}

    @Around("logPointAnnotation()||logPointMethod()")
    public Object AfterRunLog(ProceedingJoinPoint joinPoint) throws Exception {

            //获取主体的类
            MethodSignature signature=(MethodSignature) joinPoint.getSignature();
            Method method=signature.getMethod();
            LogPoint logPoint=method.getAnnotation(LogPoint.class);


        Class role=logPoint.mainRole();
            Class target=logPoint.target();
            Object[] args=joinPoint.getArgs();

            Object res = null;
            try {
               res =joinPoint.proceed();
            } catch (Throwable e) {
                res=Result.fail("未知错误");
                e.printStackTrace();
            }

            Result r= (Result) res;
            if(r.getCode()==Result.FAIL_CODE){
                return res;
            }

            SysLog sysLog=new SysLog();
            OpEnum op=logPoint.value();

            sysLog.setOperation(op.getName());
            sysLog.setOpTime(LocalDateTime.now());

            int operatorType = 0;
            int id=-1;

            if(role== Manager.class){  //管理员
                HttpSession session= (HttpSession) args[0];
                id= (Integer) session.getAttribute("user");
            }else if(role== Operator.class){
                id= CommonResource.getOperatorId();
                operatorType=1;
            }else if(role==Api.class){
                id=-1;
                Parameter[] parameters = method.getParameters();
                for(int i=0;i<parameters.length;i++){
                    Parameter param=parameters[i];
                    if(param.getType()==Api.class){
                        Api api= (Api) joinPoint.getArgs()[i];
                        LambdaQueryWrapper<Api> lqw=new LambdaQueryWrapper<>();
                        lqw.eq(Api::getApiKey,api.getApiKey());
                        api=apiService.getOne(lqw);
                        id=api.getId();
                        break;
                    }
                }
                operatorType=2;
            }else{
                throw new Exception("日志记录对象错误");
            }

            sysLog.setOperator(id);
            sysLog.setOperatorType(operatorType);
            sysLog.setTarget(classToName.get(target));

            handlerLog(sysLog);

            log.info(sysLog.toString());

            return res;
    }

    //加载日志
    @PostConstruct
    public void init(){

        //加载beans
        logService=applicationContext.getBean(SysLogService.class);
        mainInterface=applicationContext.getBean(MainInterface.class);
        sseUtil=applicationContext.getBean(SseUtil.class);

        //加载class与名称映射表
        classToName.put(Api.class,"api");
        classToName.put(Device.class,"设备");
        classToName.put(Operator.class,"操作员");
        classToName.put(WorkOrder.class,"工单");
        classToName.put(SysStatus.class,"系统状态");
        classToName.put(ConfigProperties.class,"系统配置");
        classToName.put(Warnings.class,"警告信息");
        classToName.put(DetectLog.class,"检测结果");

        typeToName.put(0,"管理员");
        typeToName.put(1,"操作人员");
        typeToName.put(2,"第三方api");

    }
    //监听器,监听操作并自动记录
    public static void writeLog(SysLog sysLog){
        logService.save(sysLog);
    }
    //输出日志信息
    public static void outputLog(SysLog sysLog){
        mainInterface.getOperationAndOutputInterface().updateOutput(sysLog,null);
        sseUtil.sendMessageToAll(String.valueOf(Result.LOG_CODE),sysLog);
    }

    public static void handlerLog(SysLog sysLog){
        if(sysLog.getMainRole()==null){
            sysLog.setMainRole(typeToName.get(sysLog.getOperatorType()).concat(sysLog.getOperator()+""));
        }
        writeLog(sysLog);
        outputLog(sysLog);
    }

    public static void outputMessage(String message){

    }
}