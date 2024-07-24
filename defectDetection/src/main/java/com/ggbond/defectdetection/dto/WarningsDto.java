package com.ggbond.defectdetection.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ggbond.defectdetection.pojo.Manager;
import com.ggbond.defectdetection.pojo.Warnings;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/3/3
 */
@Data
public class WarningsDto extends Manager {

    List<Warnings> warningsList=new ArrayList<>();

    Integer totals;

    Integer warningsSum;

    Integer oneDayWarningsSum;

    @JsonProperty("barChart")
    ChartsDto chartsDto;

    public void generateFromManager(Manager manager){
        this.setWarningsOpen(manager.isWarningsOpen());
        this.setWarningsLevel(manager.getWarningsLevel());
        this.setPhoneWay(manager.isPhoneWay());
        this.setEmailWay(manager.isEmailWay());
        this.setPhone(manager.getPhone());
        this.setEmail(manager.getEmail());
    }

}