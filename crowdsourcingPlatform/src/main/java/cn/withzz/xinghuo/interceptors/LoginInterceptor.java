package cn.withzz.xinghuo.interceptors;


import cn.withzz.xinghuo.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods",
                "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        response.setHeader("Access-Control-Allow-Headers", " Origin, X-Requested-With, Content-Type, Accept");

        response.setHeader("Access-Control-Allow-Credentials","true");


        //跨域会先发送OPTIONS预检,务必响应200 OK
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        //登陆和注册不需要拦截
        if(request.getMethod().equals("POST")){
            if(request.getRequestURI().equals("/api/user")){
                return true;
            }
            if(request.getRequestURI().equals("/api/token")){
                return true;
            }
        }

        String username = request.getHeader("username");
        String token = request.getHeader("token");
        if(username == null || token == null){
            response.sendError(401);
            return false;
        }
        String redisToken = redisService.query(username);
        if(redisToken==null || !redisToken.equals(token)){
            response.sendError(401);
            return false;
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
