package com.ggbond.defectdetection.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ggbond.defectdetection.dto.DetectResDto;
import com.ggbond.defectdetection.software.common.ConfigProperties;
import com.ggbond.defectdetection.util.ImgUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Author: 19461
 * Date: 2024/6/9
 */
@TableName("checkInfo")
@Data
@Slf4j
public class Check {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer defectnum;

    private LocalDateTime time;

    private String storagePath;

    private Integer kl=0;
    private Integer jz=0;
    private Integer ds=0;
    private Integer hh=0;
    private Integer bk=0;
    private Integer yh=0;

    @TableField(exist = false)
    private String imgBase64;


    static public Check convertTOCheck(DetectResDto dto){

        Check res=new Check();

        res.setDefectnum(dto.getDefectionsSum());
        res.setTime(LocalDateTime.now());

        String name= ImgUtil.generateRandomName();
        res.setStoragePath("./detectPicture"+"/check/"+name);

        try {
            ImgUtil.saveImageToFile(dto.getImgBase64(),res.getStoragePath());
            log.info("保存成功,路径");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("保存失败");
            return null;
        }

        List<Defection> defections=dto.getDefections();

        defections.forEach(defection -> {
            if(Objects.equals(defection.getCategory(), "kl"))
                res.setKl(res.getKl()+1);
        });
        defections.forEach(defection -> {
            if(Objects.equals(defection.getCategory(), "jz"))
                res.setJz(res.getJz()+1);
        });
        defections.forEach(defection -> {
            if(Objects.equals(defection.getCategory(), "ds"))
                res.setDs(res.getDs()+1);
        });
        defections.forEach(defection -> {
            if(Objects.equals(defection.getCategory(), "hh"))
                res.setHh(res.getHh()+1);
        });
        defections.forEach(defection -> {
            if(Objects.equals(defection.getCategory(), "bk"))
                res.setBk(res.getBk()+1);
        });
        defections.forEach(defection -> {
            if(Objects.equals(defection.getCategory(), "yh"))
                res.setYh(res.getYh()+1);
        });

        return res;
    }

}