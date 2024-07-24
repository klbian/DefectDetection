package com.ggbond.defectdetection.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.pojo.Api;
import com.ggbond.defectdetection.pojo.Manager;
import com.ggbond.defectdetection.pojo.Operator;
import com.ggbond.defectdetection.service.ManagerService;
import com.ggbond.defectdetection.service.OperatorService;
import com.ggbond.defectdetection.software.info.LogPoint;
import com.ggbond.defectdetection.software.info.OpEnum;
import com.ggbond.defectdetection.util.EncryptionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Author: 19461
 * Date: 2024/2/24
 */
@Slf4j
@RestController
@RequestMapping("/sysManage")
public class OperatorController {

    @Autowired
    OperatorService operatorService;

    @Autowired
    ManagerService managerService;

    @GetMapping("/key/info")
    public Result getOperatorInfoHandler(Integer page,Integer pageSize ){

        IPage<Operator> operatorIPage=new Page<>(page,pageSize);
        operatorService.page(operatorIPage);
        List<Operator> operatorList=operatorIPage.getRecords();
        int totalPages= Math.toIntExact(operatorService.count());

        if(operatorList!=null){
            for (Operator operator : operatorList) {
                operator.totals =totalPages;
                if(operator.getLoginPwd()!=null){
                    operator.setLoginPwd(EncryptionUtil.decrypt(operator.getLoginPwd()));
                }
                operator.setOpPwd(EncryptionUtil.decrypt(operator.getOpPwd()));
            }
            return Result.success("获取成功",operatorList);
        }else{
            return Result.fail("获取失败,请稍后再试");
        }
    }

    @PostMapping("/key/add")
    @LogPoint(value = OpEnum.Add, mainRole = Manager.class,target = Operator.class)
    public Result addOperatorHandler(HttpSession httpSession,
                                     @RequestBody Operator operator
                                     ){

        LambdaQueryWrapper<Operator> lqw=new LambdaQueryWrapper<>();
        lqw.exists("select login_pwd from operator where login_pwd is not null");

        boolean res=operatorService.exists(lqw);
        log.info("res:{}",res);

        if(!res){ //存在非空密码
            if(operator.getLoginPwd()!=null&&operatorService.count()>0){
                return Result.fail("修改失败,请检查登入密码是否为空");
            }
        }else{
            if(operator.getLoginPwd()==null){
                return Result.fail("修改失败,请检查登入密码是否为空");
            }
        }

        lqw.clear();
        lqw.eq(operator.getOpPwd()!=null,Operator::getOpPwd,EncryptionUtil.encrypt(operator.getOpPwd()))
                .or()
                .eq(operator.getJobId()!=null,Operator::getJobId,operator.getJobId());

        if(operatorService.exists(lqw)){
            return Result.fail("工号或操作密码重复");
        }

        if(operator.getLoginPwd()!=null){
            operator.setLoginPwd(EncryptionUtil.encrypt(operator.getLoginPwd()));
        }
        operator.setOpPwd(EncryptionUtil.encrypt(operator.getOpPwd()));

//        Operator operator=new Operator(jobId,loginPwd,opPwd,name,remark);

        //设置入职时间
        operator.setCreateTime(LocalDateTime.now());

        //设置创建者名称
        Integer creatorId= (Integer) httpSession.getAttribute("user");
        Manager manager=managerService.getById(creatorId);
        operator.setCreateName(manager.getName());

        operatorService.save(operator);
        lqw.clear();
        lqw.eq(Operator::getOpPwd,operator.getOpPwd());
        operator=operatorService.getOne(lqw);

        if(operator.getJobId()==null){
            operator.setJobId(operator.getId());
        }

        return Result.success("添加成功",operator);
    }

    @PutMapping("/key/batchPwd")
    @LogPoint(value = OpEnum.Update, mainRole = Manager.class,target = Operator.class)
    public Result setBatchPwdHandler(HttpSession session,
                                     @RequestParam(required = false,defaultValue = "1") Integer page,Integer pageSize,
                                     @RequestParam(required = false) String loginPwd){

        if(loginPwd!=null){
            loginPwd=EncryptionUtil.encrypt(loginPwd);
        }

        IPage<Operator> operatorIPage=new Page<>(page,pageSize);
        LambdaQueryWrapper<Operator> lqw=new LambdaQueryWrapper<>();

        Operator operator=new Operator();
        operator.setLoginPwd(EncryptionUtil.encrypt(loginPwd));
        boolean res = operatorService.update(operator, lqw);

        if(!res){
            List<Operator> operatorList=operatorService.list(operatorIPage);
            return Result.success("修改成功",operatorList);
        }else{
            return Result.fail("修改失败");
        }
    }

    @PutMapping("/key/update")
    @LogPoint(value = OpEnum.Update, mainRole = Manager.class,target = Operator.class)
    public Result updateOperatorInfoHandler(HttpSession httpSession,
                                            @RequestBody Operator operator
                                            ){

        log.info(operator.toString());
        LambdaQueryWrapper<Operator> lqw=new LambdaQueryWrapper<>();
        lqw.exists("select login_pwd from operator where login_pwd is not null");

        boolean res=operatorService.exists(lqw);

        if(!res){ //存在非空密码
            if(operator.getLoginPwd()!=null){
                return Result.fail("修改失败,请检查登入密码是否为空");
            }
        }else{
            if(operator.getLoginPwd()==null){
                return Result.fail("修改失败,请检查登入密码是否为空");
            }
        }

        if(operator.getLoginPwd()!=null){
            operator.setLoginPwd(EncryptionUtil.encrypt(operator.getLoginPwd()));
        }
        if(operator.getOpPwd()!=null){
            operator.setOpPwd(EncryptionUtil.encrypt(operator.getOpPwd()));
        }

        res = operatorService.updateById(operator);
        operator=operatorService.getById(operator.getId());

        if(res){
            return Result.success("修改成功",operator);
        }else{
            return Result.fail("修改失败,请稍后再试");
        }
    }

    @DeleteMapping("/key/delete")
    @LogPoint(value = OpEnum.Delete, mainRole = Manager.class,target = Api.class)
    public Result deleteOperator(HttpSession session,@RequestBody List<Integer> ids){
        if(ids==null){
            return null;
        }
        boolean res = operatorService.removeBatchByIds(ids);
        if(res){
            return Result.success("删除成功");
        }else{
            return Result.fail("删除失败,请稍后再试");
        }
    }
}