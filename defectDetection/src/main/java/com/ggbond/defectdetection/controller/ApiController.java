package com.ggbond.defectdetection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.pojo.Api;
import com.ggbond.defectdetection.pojo.Manager;
import com.ggbond.defectdetection.service.ApiService;
import com.ggbond.defectdetection.software.info.LogPoint;
import com.ggbond.defectdetection.software.info.OpEnum;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Author: 19461
 * Date: 2024/2/23
 */
@RestController
@Slf4j
@ResponseBody
@RequestMapping("/sysManage")
public class ApiController {

    @Autowired
    ApiService apiService;

    @GetMapping("/api/info")
    public Result getApiInfoHandler(int pageSize,int page){
        IPage<Api> apiIPage=new Page<>(page,pageSize);
        List<Api> apiList=apiService.list(apiIPage);
        int totalPages= Math.toIntExact(apiService.count());
        apiList.forEach(api -> api.totals =totalPages);
        if(apiList==null){
            return Result.fail("数据加载有误,请刷新试试");
        }else{
            return Result.success("加载成功",apiList);
        }
    }

    @LogPoint(value = OpEnum.Add, mainRole = Manager.class, target = Api.class)
    @PostMapping("/api/add")
    public Result addApiHandler(HttpSession session,
                                @RequestBody Map<String,Object> map){
        log.info(map.toString());
        Integer num= Integer.parseInt(String.valueOf(map.get("num"))) ;
        Integer validityPeriod= Integer.parseInt(Objects.equals(String.valueOf(map.get("validityPeriod")), "") ?"-1":String.valueOf(map.get("validityPeriod")));
        Integer validityTimes=Integer.parseInt(Objects.equals(String.valueOf(map.get("validityTimes")), "") ?"-1":String.valueOf(map.get("validityTimes")));
        Integer permissionLevel=Integer.parseInt(String.valueOf(map.get("permissionLevel"))) ;
        String createName= String.valueOf(map.get("createName"));

        String[] apiKeys=ApiKeyGenerator.generateApiKeys(num);

        List<Api> apiList=new LinkedList<>();

        for(String apiKey:apiKeys){
            log.info("apikey:"+apiKey);
            Api api=new Api(validityPeriod,validityTimes,permissionLevel,apiKey);
            api.setCreateName(createName);
            apiList.add(api);
        }
        apiService.saveBatch(apiList);
        apiList=apiService.list();

        return Result.success("添加成功",apiList);
    }

    @PatchMapping("/api/update")
    @LogPoint(value = OpEnum.Update, mainRole = Manager.class,target = Api.class)
    public Result updateApi(HttpSession session,
                            @RequestBody Api api){


        if(api.getValidityTimes()==0){
            api.setValidityTimes(1000+api.getValidityTimes());
        }
        if(api.getValidityPeriod()==0){
            api.setValidityPeriod(api.getValidityPeriod()+365);
        }

        boolean res=apiService.updateById(api);

        api=apiService.getById(api.getId());

        if(res){
            return Result.success("修改成功",api);
        }else{
            return Result.fail("修改失败,请稍后再试");
        }
    }

    @DeleteMapping("/api/delete")
    @LogPoint(value = OpEnum.Delete, mainRole = Manager.class,target = Api.class)
    public Result deleteApis(HttpSession session,@RequestBody(required = false) ArrayList<Integer> ids){

        if(ids==null){
            return null;
        }
        boolean res=apiService.removeBatchByIds(ids);

        if(res){
            return Result.success("删除成功");
        }else{
            return Result.fail("删除失败,请稍后再试");
        }

    }

    public class ApiKeyGenerator {
        private static final String PREFIX = "ma-";
        private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static String[] generateApiKeys(int count) {
            String[] apiKeys = new String[count];
            Random random = new Random();

            for (int i = 0; i < count; i++) {
                StringBuilder apiKey = new StringBuilder(PREFIX);

                for (int j = 0; j < 10; j++) {
                    int index = random.nextInt(CHARACTERS.length());
                    apiKey.append(CHARACTERS.charAt(index));
                }

                apiKeys[i] = apiKey.toString();
            }

            return apiKeys;
        }

    }

}