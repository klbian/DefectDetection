package com.ggbond.defectdetection.software.data;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.dto.DetectResDto;
import com.ggbond.defectdetection.pojo.Api;
import com.ggbond.defectdetection.pojo.Defection;
import com.ggbond.defectdetection.pojo.DetectLog;
import com.ggbond.defectdetection.service.*;
import com.ggbond.defectdetection.software.common.CommonResource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

/**
 * 数据处理器,生成图表,分析数据
 * <p>
 * Author: 19461
 * Date: 2024/2/16
 */
@Data
@Component("DataModule")
@Slf4j
public class DataModule {

    @Autowired
    DefectionService defectionService;

    @Autowired
    DetectLogService detectLogService;

    @Autowired
    DefectionCategoryService defectionCategoryService;

    @Autowired
    ApiService apiService;


    //x轴的节点个数
    private int xNum=5;

    //粒度
    private int granularity;

    //总缺陷个体个数
    private int totalDefectiveSum=0;

    //总缺陷数
    private static int totalDefectionsNum=0;

    //总缺陷率
    private static double defectiveRate =0.;

    //最频繁缺陷
    private static String highestOccurrenceDefect="";

    //当前粒度有缺陷数
    private int partialDefectiveSum=0;

    private Integer currentWorkOrderId;

    //数据集
    private LinkedHashMap<Integer,Double> lineData=new LinkedHashMap<>();
    private LinkedHashMap<Integer,Double> barData=new LinkedHashMap<>();
    private LinkedHashMap<String,Integer> pieData=new LinkedHashMap<>();
    private LinkedHashMap<String,Integer> apiData=new LinkedHashMap<>();
    private LinkedHashMap<String,Integer> defectionData=new LinkedHashMap<>();


    public DataModule(){
    }

    public void init(Integer workOrderId,Integer  granularity){
        this.granularity= granularity;
        currentWorkOrderId=workOrderId;
        generateChartsData(workOrderId);
    }

    public void updateDataMaps(DetectResDto res){
        if(!Objects.equals(res.getWorkOrderId(), currentWorkOrderId)){
            currentWorkOrderId=res.getWorkOrderId();
            totalDefectiveSum=0;
            partialDefectiveSum=0;
        }
        if(!res.getDefections().isEmpty()){
            totalDefectiveSum+=1;
            partialDefectiveSum+=1;
            totalDefectionsNum+=res.getDefections().size();

            for(Defection defection:res.getDefections()){
                defectionData.merge(defection.getCategory(),1,Integer::sum);
            }
        }
        if(CommonResource.getCurrentNum()%granularity==0){
            updateLineAndData(CommonResource.getCurrentNum());
        }
        updatePieData(res);
    }


    //原有基础上增量处理数据集
    public void updateLineAndData(int num){

        lineData.put(num,Math.round((totalDefectiveSum*1.0/num)*100.0)/100.0);
        barData.put(num,Math.round((partialDefectiveSum*1.0/granularity)*100.0)/100.0);
        partialDefectiveSum=0;

        lineData=modifyData(lineData);
        barData=modifyData(barData);
    }

    public void updatePieData(DetectResDto res){

        if(res.getDefections().size()>0){
            List<Defection> defections=res.getDefections();
            for(Defection defection:defections){
                String defectName=defection.getCategory();
                pieData.merge(defectName, 1, Integer::sum);
            }
            for(Map.Entry<String ,Integer> defection:pieData.entrySet()){
                if(pieData.get(highestOccurrenceDefect)!=null){
                    if(pieData.get(highestOccurrenceDefect)<defection.getValue())
                        highestOccurrenceDefect=defection.getKey();
                }else{
                    highestOccurrenceDefect=defection.getKey();
                }
            }
        }
    }
    //重新生成报表的数据(读取数据库)
    public void generateChartsData(Integer workOrderId){

        //清空数据
        lineData.clear();
        barData.clear();
        pieData.clear();
        apiData.clear();
        defectionData.clear();

        //lineData和barData
        LambdaQueryWrapper<DetectLog> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DetectLog::getWorkOrderId,workOrderId);

        int totalDetect= Math.toIntExact(detectLogService.count(lqw));

        lqw.orderBy(true,true,DetectLog::getTime);

        int baseIdx=detectLogService.count(lqw)==0?0:detectLogService.list(lqw).get(0).getId();

        lqw.gt(DetectLog::getDefectionsSum,0);

        //获取有缺陷的检测记录的ids
        List<DetectLog> detectLogs=detectLogService.list(lqw);

        List<Integer> ids=new ArrayList<>();
        for(DetectLog detectLog:detectLogs){
            ids.add(detectLog.getId());
        }




        int defectSum=0;

        for(int i=1;i*granularity<=totalDetect;i++){
            LambdaQueryWrapper<DetectLog> lqw1=new LambdaQueryWrapper<>();
            lqw1.eq(DetectLog::getWorkOrderId,workOrderId);
            lqw1.gt(DetectLog::getDefectionsSum,0);
            lqw1.gt(DetectLog::getId,baseIdx+(i-1)*granularity);
            lqw1.lt(DetectLog::getId,baseIdx+i*granularity);
//            log.info(lqw.getSqlSegment());
            int defectNum= Math.toIntExact(detectLogService.count(lqw1));
            defectSum+=defectNum;
            totalDefectiveSum+=defectNum;
            lineData.put(i*granularity,defectSum*1.0/(i*granularity));
            defectiveRate =lineData.get(i*granularity);
            barData.put(i*granularity,defectNum*1.0/granularity);
        }

        lineData=modifyData(lineData);
        barData=modifyData(barData);

        //更新pieData
        //获取所有的ids内的检测缺陷
        for(Integer i:ids){
            LambdaQueryWrapper<Defection> lqw2=new LambdaQueryWrapper<>();
            lqw2.eq(Defection::getDetectId,i);
            List<Defection> defections=defectionService.list(lqw2);
            totalDefectionsNum+=defections.size();
            for(Defection defection:defections){
                String defectName=defection.getCategory();
                pieData.merge(defectName, 1, Integer::sum);
            }
        }

        for(Map.Entry<String ,Integer> defection:pieData.entrySet()){
            if(highestOccurrenceDefect==null){
                highestOccurrenceDefect=defection.getKey();
                continue;
            }
            if(pieData.get(highestOccurrenceDefect)!=null&&pieData.get(highestOccurrenceDefect)<defection.getValue()){
                highestOccurrenceDefect=defection.getKey();
            }
        }

        //api数据处理
        LambdaQueryWrapper<Api> apiLambdaQueryWrapper=new LambdaQueryWrapper<>();
        apiLambdaQueryWrapper.orderBy(true,true,Api::getCounts);

        List<Api> apiList=apiService.list(apiLambdaQueryWrapper);

        apiList.forEach(api -> apiData.put(api.getApiKey(),api.getCounts()));

        //缺陷分类
        QueryWrapper<Defection> qw=new QueryWrapper<>();
        qw.select("category","count(*) as count")
                .groupBy("category");

        List<Map<String,Object>> res=defectionService.getBaseMapper().selectMaps(qw);

        for(Map<String,Object> map:res){
            String category=(String)map.get("category");
            Integer count=Math.toIntExact((Long) map.get("count")) ;
            defectionData.put(category,count);
        }

    }
    //根据工单号获取任意工单的数据
    public Map<String, Map> getChartsByWorkOrderId(Integer workOrderId,Integer granularity){

        if(granularity==null) {
            granularity = this.granularity;
        }
        if(workOrderId==null){
            workOrderId=currentWorkOrderId;
        }

        //数据集
        LinkedHashMap<Integer,Double> lineData=new LinkedHashMap<>();
        LinkedHashMap<Integer,Double> barData=new LinkedHashMap<>();
        LinkedHashMap<String,Integer> pieData=new LinkedHashMap<>();

        //lineData和barData
        LambdaQueryWrapper<DetectLog> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DetectLog::getWorkOrderId,workOrderId);

        int totalDetect= Math.toIntExact(detectLogService.count(lqw));

        lqw.orderBy(true,true,DetectLog::getTime);

        Integer baseIdx=detectLogService.list(lqw).get(0).getId();

        lqw.gt(DetectLog::getDefectionsSum,0);

        //获取有缺陷的检测记录的ids
        List<DetectLog> detectLogs=detectLogService.list(lqw);

        List<Integer> ids=new ArrayList<>();
        for(DetectLog detectLog:detectLogs){
            ids.add(detectLog.getId());
        }

        int defectSum=0;

        for(int i=1;i*granularity<=totalDetect;i++){
            LambdaQueryWrapper<DetectLog> lqw1=new LambdaQueryWrapper<>();
            lqw1.eq(DetectLog::getWorkOrderId,workOrderId);
            lqw1.gt(DetectLog::getDefectionsSum,0);
            lqw1.gt(DetectLog::getId,baseIdx+(i-1)*granularity);
            lqw1.lt(DetectLog::getId,baseIdx+i*granularity);
//            log.info(lqw.getSqlSegment());
            int defectNum= Math.toIntExact(detectLogService.count(lqw1));
            defectSum+=defectNum;
            totalDefectiveSum+=defectNum;
            lineData.put(i*granularity,defectSum*1.0/(i*granularity));
            defectiveRate =lineData.get(i*granularity);
            barData.put(i*granularity,defectNum*1.0/granularity);
        }

        lineData=modifyData(lineData);
        barData=modifyData(lineData);

        //更新pieData
        //获取所有的ids内的检测缺陷
        for(Integer i:ids){
            LambdaQueryWrapper<Defection> lqw2=new LambdaQueryWrapper<>();
            lqw2.eq(Defection::getDetectId,i);
            List<Defection> defections=defectionService.list(lqw2);
            //totalDefectionsNum+=defections.size();
            for(Defection defection:defections){
                String defectName=defection.getCategory();
                pieData.merge(defectName, 1, Integer::sum);
            }
        }

        Map<String,Map> dataMaps=new HashMap<>();

        dataMaps.put("lineData",lineData);
        dataMaps.put("barData",barData);
        dataMaps.put("pieData",pieData);
        dataMaps.put("apiData",apiData);
        dataMaps.put("defectionData",defectionData);
        return dataMaps;
    }

    //检测数据横坐标个数,移除多的
    public LinkedHashMap modifyData(LinkedHashMap data){
        while(data.size()>xNum){
            Iterator it=data.entrySet().iterator();
            if(it.hasNext()){
                it.next();
                it.remove();;
            }
        }
        return  data;
    }

    public Map<String, LinkedHashMap> getDataMaps(){
        if(CommonResource.getCurrentWorkOder()==null){
            generateChartsData(null);
        }else{
            generateChartsData(CommonResource.getCurrentWorkOder().getId());
        }
        Map<String,LinkedHashMap> dataMaps=new HashMap<>();
        dataMaps.put("lineData",lineData);
        dataMaps.put("barData",barData);
        dataMaps.put("pieData",pieData);
        dataMaps.put("apiData",apiData);
        dataMaps.put("defectionData",defectionData);
        return dataMaps;
    }


    public static int getTotalDefectionsNum() {
        return totalDefectionsNum;
    }

    public static double getDefectiveRate() {
        return defectiveRate;
    }

    public static String getHighestOccurrenceDefect() {
        return highestOccurrenceDefect;
    }
}