package com.ggbond.defectdetection.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggbond.defectdetection.mapper.DefectionMapper;
import com.ggbond.defectdetection.pojo.Defection;
import com.ggbond.defectdetection.service.DefectionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * Author: 19461
 * Date: 2024/2/15
 */
@Service
public class DefectionServiceImpl extends ServiceImpl<DefectionMapper,Defection> implements DefectionService {

}