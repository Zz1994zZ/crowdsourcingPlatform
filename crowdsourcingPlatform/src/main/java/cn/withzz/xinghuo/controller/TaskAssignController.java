package cn.withzz.xinghuo.controller;

import cn.withzz.crowdsourcing.base.Distribution;
import cn.withzz.crowdsourcing.base.Model;
import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;
import cn.withzz.crowdsourcing.core.TimeRangeInt;
import cn.withzz.crowdsourcing.util.MultiAssignUtil;
import cn.withzz.crowdsourcing.util.SingleAssginUtil;
import cn.withzz.xinghuo.domain.ResponseResult;
import cn.withzz.xinghuo.domain.UserInfo;
import cn.withzz.xinghuo.schedules.TaskAssigner;
import cn.withzz.xinghuo.service.TaskService;
import cn.withzz.xinghuo.service.UserInfoService;
import cn.withzz.xinghuo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Controller 实现 Restful HTTP 服务
 *
 * Created by svenzzhou on 08/11/2018.
 */
@RestController
public class TaskAssignController {

    @Autowired
    private TaskAssigner taskAssigner;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/assign", method = RequestMethod.GET)
    public List<Distribution> assign() {
        try {
            return taskAssigner.assign();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/api/sassign", method = RequestMethod.POST)
    public Task singleAssign(@RequestBody JData data) {
        System.out.println(data);
        List<Task> tasks = data.JMultiDump();
        Task task = tasks.get(0);
        System.out.println(task);
        float alpha = data.getTasks().get(0).getAlpha();
        System.out.println("alpha = "+alpha);
        try {
            SingleAssginUtil.assgin(task,alpha );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //防止死循环
        for(Model model:task.getModels()){
            model.setTask(null);
        }
        return task;
    }
    @RequestMapping(value = "/api/massign", method = RequestMethod.POST)
    public List<Task> multiAssign(@RequestBody JData data) {
        System.out.println(data);
        List<Task> tasks = data.JMultiDump();
        System.out.println(tasks);
        try {
            MultiAssignUtil.assgin(tasks,false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //防止死循环
        for(Task task:tasks){
            for(Model model:task.getModels()){
                model.setTask(null);
            }
        }
        return tasks;
    }

    @RequestMapping(value = "/api/task/{id}/assignSubmit", method = RequestMethod.POST)
    public ResponseResult singleAssignSubmit(@RequestBody Map<Integer,String> assginMap,@PathVariable("id") int id,@RequestHeader("username") String username) throws IOException {
        ResponseResult<String> result =new ResponseResult<String>();
        cn.withzz.xinghuo.domain.Task task = taskService.findByKey(id);
        ObjectMapper mapper = new ObjectMapper();
        if(!task.getCreator().equals(username)){
            result.setSuccess(false);
            result.setErrorcode("401");
            result.setMessage("您没有权限操作该任务!");
            return result;
        }
        int taskModuleNum = (int) mapper.readValue(task.getProperties(),Map.class).get("crowdNum");
        if(taskModuleNum != assginMap.size()){
            result.setSuccess(false);
            result.setErrorcode("500");
            result.setMessage("请为所有模块进行分配！");
            return result;
        }
        try{
            List executors = new ArrayList();
            for (int taskId:assginMap.keySet()){
                cn.withzz.xinghuo.domain.Task t=taskService.findByKey(taskId);
                String executor = assginMap.get(taskId);
                t.setExecutor(executor);
                t.setStatus(2);
                taskService.update(t);
                executors.add(executor);
            }
            task.setExecutor(mapper.writeValueAsString(executors));
            task.setStatus(2);
            taskService.update(task);
            result.setSuccess(true);
            result.setMessage("分配成功！");
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorcode("500");
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/api/task/{id}/assignInfo", method = RequestMethod.GET)
    public Map<String,Object> singleAssignTaskInfo(@PathVariable("id") int id,@RequestHeader("username") String username) {
        Map<String,Object> result = new HashMap<>();
        cn.withzz.xinghuo.domain.Task task = taskService.findByKey(id);
        if(!task.getCreator().equals(username))
            return null;
        List<cn.withzz.xinghuo.domain.Task> modules = taskService.findByParentTask(id);
        List<String> registers = taskService.getAllRegisters(id);
        List<UserInfo> registerInfos = new ArrayList<UserInfo>();
        for(String user:registers){
            registerInfos.add(userInfoService.findByKey(user));
        }
        result.put("modules",modules);
        result.put("registers",registerInfos);
        return result;
    }

    @RequestMapping(value = "/api/task/mutilAssignInfo", method = RequestMethod.GET)
    public List<Map<String,Object>> mutilAssignTasksInfo(@RequestHeader("username") String username) {
        //验证username是否超级管理员
        cn.withzz.xinghuo.domain.User user = userService.findByKey(username);
        if(user.getStatus()<2)
            return null;
        //获取所有任务
        List<cn.withzz.xinghuo.domain.Task> tasks = taskService.getTaskByStatus(1);
        List<Map<String,Object>> result = new ArrayList<>();
        for(cn.withzz.xinghuo.domain.Task task:tasks){
            int id = task.getId();
            List<cn.withzz.xinghuo.domain.Task> modules = taskService.findByParentTask(id);
            List<String> registers = taskService.getAllRegisters(id);
            //如果人数不够则剔除出去
            if(registers.size()==0||registers.size()<modules.size()){
                continue;
            }
            Map<String,Object> taskInfo = new HashMap<>();
            List<UserInfo> registerInfos = new ArrayList<UserInfo>();
            for(String u:registers){
                registerInfos.add(userInfoService.findByKey(u));
            }
            taskInfo.put("task",task);
            taskInfo.put("modules",modules);
            taskInfo.put("registers",registerInfos);
            result.add(taskInfo);
        }
        return result;
    }

}
//内部类JUser
class JUser{
    public int id;
    public int workTime;
    public HashMap<String,Float> skillMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public HashMap<String, Float> getSkillMap() {
        return skillMap;
    }

    public void setSkillMap(HashMap<String, Float> skillMap) {
        this.skillMap = skillMap;
    }

    @Override
    public String toString() {
        return "JUser{" +
                "id=" + id +
                ", workTime=" + workTime +
                ", skillMap=" + skillMap +
                '}';
    }
}
class JTask{
    public int id;
    public int g;
    public float alpha;
    public String skill;
    public List<JModel> models;
    public List<Integer> registers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public List<JModel> getModels() {
        return models;
    }

    public void setModels(List<JModel> models) {
        this.models = models;
    }

    public List<Integer> getRegisters() {
        return registers;
    }

    public void setRegisters(List<Integer> registers) {
        this.registers = registers;
    }

    @Override
    public String toString() {
        return "JTask{" +
                "id=" + id +
                ", g=" + g +
                ", alpha=" + alpha +
                ", skill='" + skill + '\'' +
                ", models=" + models +
                ", registers=" + registers +
                '}';
    }
}
class JModel{
    public int id;
    public float complexity;
    public JUser worker;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getComplexity() {
        return complexity;
    }

    public void setComplexity(float complexity) {
        this.complexity = complexity;
    }

    public JUser getWorker() {
        return worker;
    }

    public void setWorker(JUser worker) {
        this.worker = worker;
    }

    @Override
    public String toString() {
        return "JModel{" +
                "id=" + id +
                ", complexity=" + complexity +
                ", worker=" + worker +
                '}';
    }
}
class JData{
    public List<JTask> tasks;
    public List<JUser> users;
    public int type;

    public List<JTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<JTask> tasks) {
        this.tasks = tasks;
    }

    public List<JUser> getUsers() {
        return users;
    }

    public void setUsers(List<JUser> users) {
        this.users = users;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "JData{" +
                "tasks=" + tasks +
                ", users=" + users +
                ", type=" + type +
                '}';
    }
    //用户信息转换
    public Map<Integer, User> JUserDump(){
        HashMap<Integer, User> userMap = new HashMap<Integer, User>();
        for(JUser u:users){
            User realUser = new User();
            realUser.setId(u.getId()+"");
            realUser.setSkillMap(u.getSkillMap());
            realUser.setTimeRangeInt(new TimeRangeInt(u.getWorkTime()));
            userMap.put(u.getId(),realUser);
        }
        return userMap;
    }
    private Model JModelDump(JModel jm){
        Model model = new Model();
        model.setId(jm.getId());
        model.setComplexity(jm.getComplexity());
        return model;
    }
    private Task JTaskDump(JTask jt){
        Task task = new Task();
        task.setId(jt.getId());
        task.setSkill(jt.getSkill());
        task.setG(jt.getG());
        return task;
    }
    public Task JSingleDump(){
        JTask jt = tasks.get(0);
        if(jt!=null){
            float alpha = jt.getAlpha();
            return JMultiDump().get(0);
        }
        return null;
    }
    public List<Task> JMultiDump(){
        Map<Integer, User> userMap = JUserDump();
        List<Task> realTasks = new ArrayList<Task>(tasks.size());
        for(JTask t:tasks){
            Task realT =JTaskDump(t);
            for(int id:t.getRegisters()){
                realT.getRegister().add(userMap.get(id));
            }
            for(JModel jm: t.getModels()){
                Model rm = JModelDump(jm);
                rm.setTask(realT);
                realT.getModels().add(rm);
            }
            realTasks.add(realT);
        }
        return realTasks;
    }
}
