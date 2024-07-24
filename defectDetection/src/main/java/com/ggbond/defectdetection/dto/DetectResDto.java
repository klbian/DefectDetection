package com.ggbond.defectdetection.dto;

import com.ggbond.defectdetection.pojo.Defection;
import com.ggbond.defectdetection.pojo.DetectLog;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.util.ImgUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 检测缺陷结果类
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DetectResDto extends DetectLog {

    //base64的图像
    private String imgBase64;

    public int totals =0;

    //缺陷集
    List<Defection> defections=new ArrayList<>();

    public static List<DetectResDto> getDtoFromEntities(List<DetectLog> detectLogs){

        if(detectLogs==null){
            return null;
        }
        List<DetectResDto> detectResDtos=new LinkedList<>();

        String basePath=ConfigProperties.properties.getModelConfig().getResStoragePath();
        for(DetectLog detectLog:detectLogs){
            String filePath=detectLog.getStoragePath();
            String imgBase64= null;
            try {
                imgBase64 = ImgUtil.imageToBase64ByPath(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            DetectResDto detectResDto= DetectResDto.generateFromFather(detectLog) ;
            detectResDto.setImgBase64(imgBase64);
            detectResDtos.add(detectResDto);
        }
        return detectResDtos;
    }
    public static DetectResDto generateFromFather(DetectLog detectLog){
        DetectResDto detectResDto=new DetectResDto();
        detectResDto.setName(detectLog.getName());
        detectResDto.setId(detectLog.getId());
        detectResDto.setTime(detectLog.getTime());
        detectResDto.setDefectionsSum(detectLog.getDefectionsSum());
        detectResDto.setStoragePath(detectLog.getStoragePath());
        detectResDto.setWorkOrderId(detectLog.getWorkOrderId());
        return detectResDto;
    }
}