package com.ggbond.defectdetection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggbond.defectdetection.mapper.WarningsMapper;
import com.ggbond.defectdetection.pojo.Warnings;
import com.ggbond.defectdetection.service.WarningsService;
import com.ggbond.defectdetection.software.info.WarningsEnum;
import org.springframework.stereotype.Service;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Service
public class WarningsServiceImpl extends ServiceImpl<WarningsMapper, Warnings> implements WarningsService {

    @Override
    public void saveWarn(String content) {
        Warnings warnings=new Warnings();
        warnings.setContent(content);
        warnings.setLevel(WarningsEnum.warn.level);
        warnings.setType(WarningsEnum.warn.type);
        this.save(warnings);
    }
}