package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.Token;
import cn.withzz.xinghuo.domain.User;
import cn.withzz.xinghuo.service.RedisService;
import cn.withzz.xinghuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
    @Value("${cn.withzz.xingHuo.tokenExpireTime}")
    private long expireTime;

    @RequestMapping(value = "/api/token", method = RequestMethod.POST)
    public ResponseResult login(@RequestBody User user) {
        User realUser = userService.findByKey(user.getUsername());
        ResponseResult<Token> result =new ResponseResult<Token>();
        if(realUser!=null){
            //createTime做盐
            String salt = realUser.getCreateTime().getTime()+"";
            String password = user.getPassword()+salt;
            String encodeStr = DigestUtils.md5DigestAsHex(password.getBytes());
            //校验密码是否正确
            if(encodeStr.equals(realUser.getPassword())){
                Token token = new Token();
                token.setUsername(user.getUsername());
                token.setCreateTime(new Timestamp(System.currentTimeMillis()));
                token.setUpdateTime(token.getCreateTime());
                token.setExprieTime(expireTime);
                token.setType(Token.Tpye.USER);
                redisService.saveToken(token);
                result.setData(token);
                result.setMessage("登陆成功！");
                result.setSuccess(true);
                return result;
            }
        }
        result.setSuccess(false);
        result.setErrorcode("");
        result.setMessage("账号或密码错误！");
        return result;
    }

    @RequestMapping(value = "/api/token", method = RequestMethod.DELETE)
    public ResponseResult logout(@RequestHeader HttpHeaders headers) {
        String username = headers.getFirst("username");
        ResponseResult<String> result =new ResponseResult<String>();
        result.setData(username);
        try{
            redisService.delete(username);
            result.setSuccess(true);
            result.setMessage("注销成功！");
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage("注销失败！");
        }
        return result;
    }
}
