package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.Token;
import cn.withzz.xinghuo.domain.User;
import cn.withzz.xinghuo.service.TokenService;
import cn.withzz.xinghuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Token Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 08/11/2018.
 */
@RestController
public class TokenRestController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/api/token", method = RequestMethod.POST)
    public String login(@RequestBody User user) {
        User realUser = userService.findByKey(user.getUsername());
        //createTime做盐
        String salt = realUser.getCreateTime().getTime()+"";
        String password = user.getPassword()+salt;
        String encodeStr = DigestUtils.md5DigestAsHex(password.getBytes());
        if(encodeStr.equals(realUser.getPassword())){
            Token token = new Token();
            token.setUsername(user.getUsername());
            token.setCreateTime(new Date());
            token.setUpdateTime(token.getCreateTime());
            token.setExprieTime(7200L);
            token.setType(1);
            tokenService.add(token);
            return "OK";
        }else
        {
            return "fails";
        }
    }

    @RequestMapping(value = "/api/token/{token}", method = RequestMethod.DELETE)
    public void logout(@PathVariable("token") String token) {
        tokenService.delete(token);
    }
}
