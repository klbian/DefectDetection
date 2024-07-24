package com.ggbond.defectdetection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author: 19461
 * Date: 2024/2/28
 */
@Data
public class SourceDto {

    List xAxisData;
    List seriesData;
    List yAxisData;
    List legendData;

    public static SourceDto generateXYSeries(Map<Integer,Double> map){
        SourceDto sourceDto=new SourceDto();
        List x=new LinkedList();
        List y=new LinkedList();
        for(Map.Entry<Integer,Double> entry:map.entrySet()){
            x.add(entry.getKey());
            y.add(entry.getValue());
        }
        sourceDto.setXAxisData(x);
        sourceDto.setSeriesData(y);

        return sourceDto;
    }

    public static SourceDto generateRingData(Map<String,Integer> map){

        List keyList=new LinkedList();
        List<SeriesDto> seriesDtoList=new LinkedList<>();

        for(Map.Entry<String,Integer> entry:map.entrySet()){
            keyList.add(entry.getKey());
            seriesDtoList.add(new SeriesDto(entry.getKey(), entry.getValue()));
        }
        SourceDto sourceDto=new SourceDto();
        sourceDto.setLegendData(keyList);
        sourceDto.setSeriesData(seriesDtoList);

        return sourceDto;
    }

    @AllArgsConstructor
    static
    class SeriesDto{
        String name;
        int value;
    }


}