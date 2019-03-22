package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.Task;
import cn.withzz.xinghuo.domain.UserInfo;
import cn.withzz.xinghuo.service.TaskService;
import cn.withzz.xinghuo.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * Register Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 14/12/2018.
 */
@RestController
public class RegisterController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserInfoService userInfoService;

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
    public List<Task> getTasksByUsername(@PathVariable("username") String username,@RequestParam("status") int status) {
        List<Task> tasks = taskService.getRegisterTasks(username,status);
        return tasks;
    }

    //随机挑一批人强行报名
    @RequestMapping(value = "/api/randomRegister", method = RequestMethod.GET)
    public ResponseResult randomRegister() throws IOException {
        ResponseResult<String> result =new ResponseResult<String>();
        List<Task> tasks = taskService.getTaskByStatus(1);
        int sumNum  = 0;
        for(Task task : tasks){
            ObjectMapper mapper = new ObjectMapper();
            int taskModuleNum = (int) mapper.readValue(task.getProperties(),Map.class).get("crowdNum");
            sumNum+=taskModuleNum;
        }
        //随机挑选sumNum个人数的工人
        List<UserInfo> users = userInfoService.findAll();
        int usersNum = users.size();
        Random random = new Random();
        List<UserInfo> selectedUsers = new ArrayList<UserInfo>();
        for(int i = 0; i < sumNum && i < usersNum; i++){
            int selectedIndex = random.nextInt(usersNum-i);
            selectedUsers.add(users.get(selectedIndex));
            UserInfo temp = users.get(usersNum-i-1);
            users.set(usersNum-i-1,users.get(selectedIndex));
            users.set(selectedIndex,temp);
        }
        for(Task task : tasks){
          for(UserInfo userInfo:selectedUsers){
              taskService.register(task.getId(),userInfo.getUsername());
          }
        }
        result.setMessage("注册成功，每个任务添加"+selectedUsers);
        result.setSuccess(true);
        return result;
    }



}
