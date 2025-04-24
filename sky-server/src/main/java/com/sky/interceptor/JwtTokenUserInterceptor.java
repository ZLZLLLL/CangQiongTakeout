package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.beancontext.BeanContext;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler){
        //判断当前拦截对象,如果拦截对象不是HandlerMethod类型，则不进行拦截
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        //1、获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2.校验令牌
        try {
            log.info("校验令牌：{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userid = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户id：{}", userid);
            //保留当前线程用户id
            BaseContext.setCurrentId(userid);
            //放行
            return true;
        }catch (Exception ex){
            //校验未通过，响应状态码401
            response.setStatus(401);
            return false;
        }
    }
}
