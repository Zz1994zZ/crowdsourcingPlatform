package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.Task;
import cn.withzz.xinghuo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Register Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 14/12/2018.
 */
@RestController
public class RegisterController {

    @Autowired
    private TaskService taskService;
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
    //获取用户报名的任务
    @RequestMapping(value = "/api/user/{username}/registerTask", method = RequestMethod.GET)
    public List<Task> getTasksByUsername(@PathVariable("username") String username) {
        List<Task> tasks = taskService.getRegisterTasks(username,1);
        return tasks;
    }


}
