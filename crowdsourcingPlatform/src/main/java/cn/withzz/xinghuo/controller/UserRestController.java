package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.User;
import cn.withzz.xinghuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 08/11/2018.
 */
@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/user/{username}", method = RequestMethod.GET)
    public User findOne(@PathVariable("username") String username) {
        return userService.findByKey(username);
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody User user) {
        ResponseResult<String> result =new ResponseResult<String>();
        try{
            userService.save(user);
            result.setMessage("注册成功！");
            result.setSuccess(true);
        }catch (Exception e){
            result.setMessage("注册失败！");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    public void modify(@RequestBody User user) {
        userService.update(user);
    }

    @RequestMapping(value = "/api/user/{username}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("username") String usernmae) {
        userService.delete(usernmae);
    }
}
