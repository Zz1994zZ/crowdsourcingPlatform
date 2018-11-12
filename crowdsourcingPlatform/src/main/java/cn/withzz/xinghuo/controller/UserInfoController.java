package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.UserInfo;
import cn.withzz.xinghuo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 08/11/2018.
 */
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/api/userInfo/{username}", method = RequestMethod.GET)
    public UserInfo findOne(@PathVariable("username") String username) {
        return userInfoService.findByKey(username);
    }

    @RequestMapping(value = "/api/userInfo", method = RequestMethod.GET)
    public List<UserInfo> findAll() {
        return userInfoService.findAll();
    }

    @RequestMapping(value = "/api/userInfo", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody UserInfo userInfo,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();
        //校验身份
        if(username.equals(userInfo.getUsername())){
            try{
                userInfoService.save(userInfo);
                result.setMessage("添加用户信息成功！");
                result.setSuccess(true);
            }catch (Exception e){
                result.setMessage("添加用户信息失败！");
                result.setSuccess(false);
            }
        }else{
            result.setMessage("添加用户信息失败！无权操作该用户！");
            result.setSuccess(false);
        }

        return result;
    }

    @RequestMapping(value = "/api/userInfo", method = RequestMethod.PUT)
    public ResponseResult modify(@RequestBody UserInfo userInfo,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();

        //校验身份
        if(username.equals(userInfo.getUsername())){
            try{
                userInfoService.update(userInfo);
                result.setMessage("修改用户信息成功！");
                result.setSuccess(true);
            }catch (Exception e){
                result.setMessage("修改用户信息失败！");
                result.setSuccess(false);
            }
        }else{
            result.setMessage("修改用户信息失败！无权操作该用户！");
            result.setSuccess(false);
        }
        return result;

    }

    @RequestMapping(value = "/api/userInfo/{username}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("username") String usernmae) {
        userInfoService.delete(usernmae);
    }
}
