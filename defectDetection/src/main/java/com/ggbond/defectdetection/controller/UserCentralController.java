package com.ggbond.defectdetection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.pojo.Manager;
import com.ggbond.defectdetection.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Author: 19461
 * Date: 2024/2/24
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserCentralController {

    @Autowired
    ManagerService managerService;

    @PutMapping("/saveInfo")
    public Result savePropertiesHandler(Integer id,
                                        @RequestParam(required = false) String account,
                                        @RequestParam(required = false) String phoneNumber,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email){
        Manager manager=new Manager(id,account,phoneNumber,name,email);

        LambdaQueryWrapper<Manager> lqw=new LambdaQueryWrapper<>();

        lqw.eq(Manager::getAccount,account);
        if(managerService.count(lqw)>0){
            return Result.fail("账号重复,请换过一个");
        }

        boolean res = managerService.updateById(manager);

        manager=managerService.getById(id);

        if(res){
            return Result.success("保存成功",manager);
        }else{
            return Result.fail("保存失败,请稍后再试");
        }
    }

    @PutMapping("/savePwd")
    public Result savePwdHandler(HttpSession session,String oldPwd,String newPwd){
        oldPwd= DigestUtils.md5DigestAsHex(oldPwd.getBytes());

        Integer id= session.getAttribute("user")==null?1:(Integer) session.getAttribute("user");

        String correctPwd=managerService.getById(id).getPwd();

        if(Objects.equals(correctPwd, oldPwd)){
            newPwd=DigestUtils.md5DigestAsHex(newPwd.getBytes());
            Manager manager=new Manager();
            manager.setId(id);
            manager.setPwd(newPwd);
            boolean res = managerService.updateById(manager);
            if(res){
                return Result.success("修改成功");
            }else{
                return Result.fail("修改密码失败");
            }
        }else{
            return Result.fail("修改密码失败");
        }

    }

}