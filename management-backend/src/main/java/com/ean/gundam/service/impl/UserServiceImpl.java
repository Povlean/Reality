package com.ean.gundam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ean.gundam.commons.Result;
import com.ean.gundam.constants.CommonConstant;
import com.ean.gundam.constants.ResCode;
import com.ean.gundam.mapper.UserMapper;
import com.ean.gundam.model.domain.User;
import com.ean.gundam.model.request.LoginRequest;
import com.ean.gundam.model.request.RegisterRequest;
import com.ean.gundam.service.UserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Asphyxia
* @description 针对表【tb_user】的数据库操作Service实现
* @createDate 2023-04-22 11:17:06
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public Result<User> userLogin(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        // 密码和用户名都不为空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return new Result(ResCode.ERROR,"用户名或密码不能为空",null);
        }
        User user = userMapper.userLogin(username, password);
        if (user == null) {
            return new Result(ResCode.ERROR,"用户查询有误");
        }
        httpServletRequest.getSession().setAttribute(CommonConstant.USER_LOGIN_STATE,user);
        return new Result(ResCode.SUCCESS,"登录成功",user);
    }

    @Override
    public Result userRegister(RegisterRequest registerReq) {
        String username = registerReq.getUsername();
        String checkPwd = registerReq.getCheckPwd();
        String password = registerReq.getPassword();
        // 账号或密码为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(checkPwd)) {
            return new Result(ResCode.ERROR,"用户名密码不能为空");
        }
        // 如果密码和确认密码的长度一样
        if (checkPwd.length() != password.length() || !checkPwd.equals(password)) {
            return new Result(ResCode.ERROR,"密码和确认密码校验有误");
        }
        // 校验密码长度
        if (password.length() < 6) {
            return new Result(ResCode.ERROR,"密码长度有误");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername,username);
        User sameUser = this.getOne(queryWrapper);
        // 数据库中已有该用户
        if (sameUser != null) {
            return new Result(ResCode.ERROR,"用户已存在，不能重复注册");
        }
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        boolean save = this.save(user);
        // 如果save失败则返回
        if (!save) {
            return new Result(ResCode.ERROR,"内部问题");
        }
        // 返回结果
        return new Result(ResCode.SUCCESS,"注册成功",user);
    }

    @Override
    public Result logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute(CommonConstant.USER_LOGIN_STATE);
        return new Result(ResCode.SUCCESS,"登出成功");
    }

    @Override
    public Result getAllUser() {
        List<User> users = userMapper.getAllUser();
        users = users.stream().map((user) -> {
            user = getSafetyUser(user);
            return user;
        }).collect(Collectors.toList());
        return new Result(ResCode.SUCCESS,"查询成功",users);
    }

    public User getSafetyUser(User user) {
        User safetyUser = new User();
        safetyUser.setUsername(user.getUsername());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setStatus(user.getStatus());
        safetyUser.setAvatar(user.getAvatar());
        return safetyUser;
    }

}




