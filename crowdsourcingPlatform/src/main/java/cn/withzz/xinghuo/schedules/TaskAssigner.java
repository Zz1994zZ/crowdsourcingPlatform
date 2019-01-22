package cn.withzz.xinghuo.schedules;


import cn.withzz.crowdsourcing.base.Distribution;
import cn.withzz.crowdsourcing.base.Model;
import cn.withzz.crowdsourcing.base.User;
import cn.withzz.crowdsourcing.core.KMimplement;
import cn.withzz.crowdsourcing.core.TimeRangeInt;
import cn.withzz.xinghuo.dao.RegisterDao;
import cn.withzz.xinghuo.dao.TaskDao;
import cn.withzz.xinghuo.dao.UserInfoDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskAssigner {
    @Autowired
    TaskDao taskDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    RegisterDao registerDao;

    private ObjectMapper mapper = new ObjectMapper();
    private List<cn.withzz.xinghuo.domain.Task> tasksFilter(){
        //这里只返回满足特定要求的非子任务
//        List<cn.withzz.xinghuo.domain.Task> tasks = taskDao.findByCondition(null);
        List<cn.withzz.xinghuo.domain.Task> tasks = taskDao.findAll();
        return tasks;
    }

    private  List<cn.withzz.crowdsourcing.base.Task>  tasksDump (List<cn.withzz.xinghuo.domain.Task> tasks){
        //TODO
        List<cn.withzz.crowdsourcing.base.Task> assginTasks = new ArrayList<cn.withzz.crowdsourcing.base.Task>(tasks.size());
        for (cn.withzz.xinghuo.domain.Task task: tasks) {
            //过滤子任务,父任务id不为0则为子任务
            if(task.getParentTask()!=0){
                continue;
            }
            //获取所有子任务
            List<cn.withzz.xinghuo.domain.Task> modules = taskDao.findByParentTask(task.getId());
            //主体任务
            cn.withzz.crowdsourcing.base.Task t = new cn.withzz.crowdsourcing.base.Task();
            String properties = task.getProperties();

            try {
                Map<String,Object> userData = mapper.readValue(properties, Map.class);
                t.setId(task.getId());
                t.setSkill((String)userData.get("skills"));
                //多模块任务
                if(!userData.get("crowdNum").equals(1)){
                    for (cn.withzz.xinghuo.domain.Task module: modules) {
                        Model model = new Model(0.7f,t.getSkill(),t);
                        model.setId(t.getId());
                        t.getModels().add(model);
                    }
                }else{//单模块任务
                    Model model = new Model(0.7f,t.getSkill(),t);
                    t.getModels().add(model);
                }
                assginTasks.add(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return assginTasks;
    }

    private  List<cn.withzz.crowdsourcing.base.User>  usersDump (List<cn.withzz.xinghuo.domain.UserInfo> users,Map<String, cn.withzz.crowdsourcing.base.User> userMap) throws Exception {
        List<cn.withzz.crowdsourcing.base.User> dumpUsers = new ArrayList<cn.withzz.crowdsourcing.base.User>();
        for(cn.withzz.xinghuo.domain.UserInfo user:users) {
            String username = user.getUsername();
            cn.withzz.crowdsourcing.base.User dumpUser = userMap.get(username);
            if(dumpUser==null){
                dumpUser = new cn.withzz.crowdsourcing.base.User();
                dumpUser.setId(user.getUsername());
                dumpUser.setNickname(user.getName());
                //获取技能map
                HashMap<String,Float> skillList = mapper.readValue( user.getSkillList(), HashMap.class);
                dumpUser.setSkillMap(skillList);
                //获取活跃时间
                HashMap<String,Object> extention = mapper.readValue( user.getExtention(), HashMap.class);
                ArrayList<Integer> times =  (ArrayList<Integer>)extention.get("activeTime");
                TimeRangeInt tri = new TimeRangeInt();
                tri.addRange(times.get(0),times.get(1));
                dumpUser.setTimeRangeInt(tri);
                userMap.put(username,dumpUser);
            }
            dumpUsers.add(dumpUser);
        }
        return dumpUsers;
    }

    public List<Distribution>  assign() throws Exception {
        Map<String,cn.withzz.crowdsourcing.base.User> userMap = new HashMap<String, cn.withzz.crowdsourcing.base.User>();
        List<cn.withzz.xinghuo.domain.Task> tasks = tasksFilter();
        List<cn.withzz.crowdsourcing.base.Task> tranTasks = tasksDump(tasks);
        for (cn.withzz.crowdsourcing.base.Task task: tranTasks) {
            List<String> usernames = registerDao.findByTask(task.getId());
            List<cn.withzz.xinghuo.domain.UserInfo> userInfos = new ArrayList<cn.withzz.xinghuo.domain.UserInfo>();
            for (String username: usernames) {
                userInfos.add(userInfoDao.findByKey(username));
            }
            List<cn.withzz.crowdsourcing.base.User> transUsers = usersDump(userInfos,userMap);
            task.setRegister(transUsers);
        }
        return new KMimplement(tranTasks,true).KM();
    }

}

