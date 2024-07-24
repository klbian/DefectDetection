package com.ggbond.defectdetection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ggbond.defectdetection.pojo.WorkOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
}
