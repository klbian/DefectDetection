package com.ggbond.defectdetection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.dto.ChartsDto;
import com.ggbond.defectdetection.dto.CheckDto;
import com.ggbond.defectdetection.pojo.Check;
import com.ggbond.defectdetection.service.CheckService;
import com.ggbond.defectdetection.util.ImgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/6/9
 */
@RestController
@RequestMapping("/check")
@Slf4j
@CrossOrigin("*")
public class CheckController {

    @Autowired
    CheckService checkService;

    @GetMapping("/info")
    public Result getCheckInfosHandler(int page,int pageSize){

        IPage<Check> pg=new Page<>(page,pageSize);
        int totalPages= (int) Math.ceil((checkService.count()*1.0)/pageSize);

        CheckDto dto=new CheckDto();

        dto.setTotalPages(totalPages);

        List<Check> checkList=checkService.list(pg);
        checkList.forEach(check -> {
            try {
                String imgBase64= ImgUtil.imageToBase64ByPath(check.getStoragePath());
                check.setImgBase64(imgBase64);
            } catch (IOException e) {
                check.setImgBase64("");
                e.printStackTrace();
            }
        });
        dto.setCheckList(checkList);

        return Result.success("获取成功",dto);
    }

}