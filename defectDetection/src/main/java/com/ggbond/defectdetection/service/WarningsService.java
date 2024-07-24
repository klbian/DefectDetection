package com.ggbond.defectdetection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ggbond.defectdetection.pojo.Warnings;
import org.springframework.stereotype.Service;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Service
public interface WarningsService extends IService<Warnings> {
    void saveWarn(String content);
}