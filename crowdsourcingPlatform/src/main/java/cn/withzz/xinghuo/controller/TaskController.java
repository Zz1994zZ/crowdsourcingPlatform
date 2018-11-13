package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.Task;
import cn.withzz.xinghuo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * User Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 08/11/2018.
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/api/task/{id}", method = RequestMethod.GET)
    public Task findOne(@PathVariable("id") int id) {
        return taskService.findByKey(id);
    }

    @RequestMapping(value = "/api/task", method = RequestMethod.GET)
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @RequestMapping(value = "/api/task", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody Task task,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();
            try{
                task.setCreator(username);
                taskService.save(task);
                result.setMessage("添加任务信息成功！");
                result.setSuccess(true);
            }catch (Exception e){
                result.setMessage("添加任务信息失败！");
                result.setSuccess(false);
            }
        return result;
    }

    @RequestMapping(value = "/api/task", method = RequestMethod.PUT)
    public ResponseResult modify(@RequestBody Task task,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();

        //校验身份
        if(username.equals(task.getCreator())){
            try{
                taskService.update(task);
                result.setMessage("修改任务信息成功！");
                result.setSuccess(true);
            }catch (Exception e){
                result.setMessage("修改任务信息失败！");
                result.setSuccess(false);
            }
        }else{
            result.setMessage("修改用户信息失败！无权操作该用户！");
            result.setSuccess(false);
        }
        return result;

    }

    @RequestMapping(value = "/api/task/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") int id) {
        taskService.delete(id);
    }
}
