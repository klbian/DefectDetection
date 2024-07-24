package com.ggbond.defectdetection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ggbond.defectdetection.pojo.Warnings;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarningsMapper extends BaseMapper<Warnings> {
    void saveWarn(String content);
}
