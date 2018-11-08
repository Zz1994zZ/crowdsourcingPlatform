package cn.withzz.xinghuo.controller;

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
    public void create(@RequestBody User user) {
        System.out.print(user.getUsername());
        System.out.print(user.getPassword());
        userService.save(user);
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    public void modify(@RequestBody User user) {
        userService.update(user);
    }

    @RequestMapping(value = "/api/user/{username}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("username") Long id) {
        userService.delete(id);
    }
}
