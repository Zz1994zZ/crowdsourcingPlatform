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
        if(task.getParentTask()!=0){
            Task parentTask = taskService.findByKey(task.getParentTask());
            if(parentTask==null||!parentTask.getCreator().equals(username)){
                result.setMessage("没有权限为该任务添加子任务或该任务不存在！");
                result.setSuccess(false);
                return result;
            }
        }
        try{
            task.setCreator(username);
            taskService.save(task);
            result.setMessage("添加任务信息成功！");
            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            result.setMessage("添加任务信息失败！");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/api/task", method = RequestMethod.PUT)
    public ResponseResult modify(@RequestBody Task task,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();
        Task oldTask = taskService.findByKey(task.getId());
        //校验身份
        if(oldTask==null||!oldTask.getCreator().equals(username)){
            result.setMessage("修改任务信息失败！任务不存在或无权操作该任务！");
            result.setSuccess(false);
        }else{
            try{
                taskService.update(task);
                result.setMessage("修改任务信息成功！");
                result.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
                result.setMessage("修改任务信息失败！");
                result.setSuccess(false);
            }
        }
        return result;

    }

    //报名任务
    @RequestMapping(value = "/api/task/{id}/user", method = RequestMethod.POST)
    public ResponseResult register(@PathVariable("id") int id,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();
        Task task = taskService.findByKey(id);
        //校验任务是否存在
        if(task==null){
            result.setMessage("任务不存在！");
            result.setSuccess(false);
        }else{
            try{
                taskService.register(task.getId(),username);
                result.setMessage("报名任务成功！");
                result.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
                result.setMessage("报名任务失败！");
                result.setSuccess(false);
            }
        }
        return result;
    }


    @RequestMapping(value = "/api/task/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") int id) {
        taskService.delete(id);
    }
}
