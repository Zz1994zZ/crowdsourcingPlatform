package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.Token;
import cn.withzz.xinghuo.domain.User;
import cn.withzz.xinghuo.service.RedisService;
import cn.withzz.xinghuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

/**
 * Token Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 08/11/2018.
 */
@RestController
public class TokenRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;



    @RequestMapping(value = "/api/token", method = RequestMethod.POST)
    public ResponseResult login(@RequestBody User user) {
        User realUser = userService.findByKey(user.getUsername());
        //createTime做盐
        String salt = realUser.getCreateTime().getTime()+"";
        String password = user.getPassword()+salt;
        String encodeStr = DigestUtils.md5DigestAsHex(password.getBytes());
        ResponseResult<Token> result =new ResponseResult<Token>();
        //校验密码是否正确
        if(encodeStr.equals(realUser.getPassword())){
            Token token = new Token();
            token.setUsername(user.getUsername());
            token.setCreateTime(new Timestamp(System.currentTimeMillis()));
            token.setUpdateTime(token.getCreateTime());
            token.setExprieTime(3600L);
            token.setType(Token.Tpye.USER);
            String exitToken = redisService.query(token.getUsername());
            redisService.saveToken(token);
            result.setData(token);
            result.setMessage("登陆成功！");
            result.setSuccess(true);
        }else
        {
            result.setSuccess(false);
            result.setErrorcode("");
            result.setMessage("账号或密码错误！");
        }
        return result;
    }

    @RequestMapping(value = "/api/token/{token}", method = RequestMethod.DELETE)
    public void logout(@PathVariable("token") String username) {
        redisService.delete(username);
    }
}