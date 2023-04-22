package com.ean.gundam.controller;

import com.ean.gundam.commons.Result;
import com.ean.gundam.constants.ResCode;
import com.ean.gundam.model.domain.User;
import com.ean.gundam.model.request.LoginRequest;
import com.ean.gundam.model.request.RegisterRequest;
import com.ean.gundam.service.UserService;
import com.xiaoleilu.hutool.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description:TODO
 * @author:Povlean
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<User> userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        if (ObjectUtil.isNull(loginRequest)) {
            return new Result(ResCode.ERROR,"登录信息为空");
        }
        return userService.userLogin(loginRequest,httpServletRequest);
    }

    @PostMapping("/register")
    public Result userRegister(@RequestBody RegisterRequest registerReq) {
        if (ObjectUtil.isNull(registerReq)) {
            return new Result(ResCode.ERROR,"注册信息为空");
        }
        return userService.userRegister(registerReq);
    }

    @PostMapping("/logout")
    public Result userLogout (HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return new Result(ResCode.ERROR,"请求错误");
        }
        return userService.logout(httpServletRequest);
    }

    /**
    * @description:TODO
    * @author:Povlean
    * @date:2023/4/22 21:27
    * @param:* @param null
    * @return:
    */
    @GetMapping("/list")
    public Result getAllUser() {
        return userService.getAllUser();
    }

}
