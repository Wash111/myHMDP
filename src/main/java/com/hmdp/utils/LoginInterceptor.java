package com.hmdp.utils;

import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //前置拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //1.获取请求头中的token
        String token = request.getHeader("authorization");
        if(StrUtil.isBlankIfStr(token)){
            //拦截，返回401状态码
            response.setStatus(401);
            return false;
        }
        //2基于token获取redis中的用户
        Map<Object,Object> userMap =  stringRedisTemplate.opsForHash().entries(token);
        //3.判断用户是否存在

        //将查询到的Hash数据转为UserDTO对象
        //5.存在，保存用户信息到Threadlocal

        //TODO 刷新TOKEN有效期



        //6.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //7.清除Threadlocal
        UserHolder.removeUser();
    }

}