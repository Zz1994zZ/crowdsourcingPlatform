package cn.withzz.xinghuo.controller;

import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.Task;
import cn.withzz.xinghuo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

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
    public Map<String,Object> findOne(@PathVariable("id") int id) {
        Map<String,Object> result = new HashMap<>();
        Task task = taskService.findByKey(id);
        List<String> registers = taskService.getAllRegisters(id);
        result.put("task",task);
        result.put("registers",registers);
        return result;
    }

    @RequestMapping(value = "/api/task/{id}/taskModules", method = RequestMethod.GET)
    public Map<String,Object> findOnesModules(@PathVariable("id") int id) {
        //输入是父任务id
        Map<String,Object> result = new HashMap<>();
        List<Task> modules = taskService.findByParentTask(id);
        result.put("modules",modules);
        return result;
    }

    @RequestMapping(value = "/api/task", method = RequestMethod.GET)
    public Map<String,Object> findAll(String page,String per_page) {
        Map<String,Object> result = new HashMap<>();
        List<Task>  tasks = null;
        if(page==null&&per_page==null){
            tasks = taskService.findAll();
        }else{
            try{
                int iPage = Integer.parseInt(page);
                int iPer_page = Integer.parseInt(per_page);
                if(iPage>0&&iPer_page>0){
                    tasks = taskService.findByPage(iPage,iPer_page);
                }
            }catch (Exception e){
                tasks = taskService.findByPage(1,10);
            }
        }
        int count = taskService.getCount("where parentTask = 0");
        result.put("count",count);
        result.put("tasks",tasks);
        return result;
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

    @RequestMapping(value = "/api/taskModules", method = RequestMethod.POST)
    public ResponseResult createModules(@RequestBody List<Task> taskModules,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();
        if(taskModules.size()<=1){//单人任务
            result.setMessage("请使用 /api/task 添加单人任务");
            result.setSuccess(false);
        }else {//多模块任务
            try{
                Task mainTask = taskModules.get(0);
                mainTask.setCreator(username);
                taskService.save(mainTask);
                for (int i = 1; i < taskModules.size(); i++) {
                    Task module = taskModules.get(i);
                    module.setType(0);
                    taskService.saveModule(mainTask,module);
                }
                result.setMessage("添加任务信息成功！");
                result.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
                result.setMessage("添加任务信息失败！");
                result.setSuccess(false);
            }

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

    @RequestMapping(value = "/api/task/{id}/submit", method = RequestMethod.POST)
    public ResponseResult submit(@PathVariable("id") int id,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();
        Task task = taskService.findByKey(id);
        //校验身份
        if(task==null||!task.getExecutor().equals(username)){
            result.setMessage("提交任务失败！任务不存在或无权操作该任务！");
            result.setSuccess(false);
        }else{
            try{
                //TODO  差一张日志表记录状态更改日志
                task.setStatus(3);
                taskService.update(task);
                result.setMessage("提交任务成功！");
                result.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
                result.setMessage("提交任务失败！");
                result.setSuccess(false);
            }
        }
        return result;
    }

    @RequestMapping(value = "/api/task/grade", method = RequestMethod.POST)
    public ResponseResult grade(@RequestBody Task task,@RequestHeader("username") String username) {
        ResponseResult<String> result =new ResponseResult<String>();
        Task oldTask = taskService.findByKey(task.getId());
        //校验身份
        if(oldTask==null||!oldTask.getCreator().equals(username)){
            result.setMessage("评价失败！任务不存在或无权操作该任务！");
            result.setSuccess(false);
        }else{
            try{
                //TODO  差一张日志表记录状态更改日志
                oldTask.setProperties(task.getProperties());
                taskService.update(oldTask);
                result.setMessage("评价任务成功！");
                result.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
                result.setMessage("评价任务失败！");
                result.setSuccess(false);
            }
        }
        return result;
    }



    @RequestMapping(value = "/api/task/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") int id) {
        taskService.delete(id);
    }

    @RequestMapping(value = "/api/user/{username}/publishedTask", method = RequestMethod.GET)
    public List<Task> findOnesTasks(@PathVariable("username") String username,@RequestParam("status") int status) {
        List<Task> tasks = taskService.getPublishedTasks(username,status);
        return tasks;
    }

    @RequestMapping(value = "/api/user/{executor}/task", method = RequestMethod.GET)
    public  List<Task> userTasks(@PathVariable("executor") String executor,@RequestParam("status") int status) {
        List<Task> tasks = taskService.getTaskByExcutor(executor,status);
        return tasks;
    }
}
