package com.ggbond.defectdetection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ggbond.defectdetection.common.BaseContext;
import com.ggbond.defectdetection.common.Result;
import com.ggbond.defectdetection.pojo.Manager;
import com.ggbond.defectdetection.service.ManagerService;
import com.ggbond.defectdetection.software.info.LogPoint;
import com.ggbond.defectdetection.software.info.OpEnum;
import com.ggbond.defectdetection.util.SseUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


/**
 * Author: 19461
 * Date: 2024/2/23
 */
@RestController
@Slf4j
@ResponseBody
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginController {

    @Autowired
    ManagerService managerService;

    @Autowired
    SseUtil sseUtil;

    @PostMapping("/in")
    @LogPoint(value = OpEnum.Login, mainRole = Manager.class,target = Manager.class)
    public Result<Manager> loginInHandler(HttpSession httpSession, @RequestBody Manager req){

        String account=req.getAccount();
        String pwd=req.getPwd();

        LambdaQueryWrapper<Manager> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Manager::getAccount,account);

        //密码加密
        pwd= DigestUtils.md5DigestAsHex(pwd.getBytes());
        lqw.eq(Manager::getPwd,pwd);

        Manager manager=managerService.getOne(lqw);

        if(manager!=null){
//            if(manager.isOnline()){
//                return Result.fail("已经在线");
//            }else{
                Manager manager1=new Manager();
                manager1.setId(manager.getId());
                manager1.setOnline(true);
                managerService.updateById(manager1);

                BaseContext.setCurrentId((long)manager.getId());
                httpSession.setAttribute("user",manager.getId());
                sseUtil.connect((long)manager.getId());

                return Result.success("登入成功",manager);
//            }
        }else{
            return Result.fail("登入失败,请检查账号或密码");
        }
    }

    @GetMapping("/out")
    public void loginOutHandler(HttpSession session,String account){

        session.removeAttribute("user");

        LambdaQueryWrapper<Manager> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Manager::getAccount,account);


        Manager manager=new Manager();
        manager.setOnline(false);

        managerService.update(manager,lqw);
    }

}