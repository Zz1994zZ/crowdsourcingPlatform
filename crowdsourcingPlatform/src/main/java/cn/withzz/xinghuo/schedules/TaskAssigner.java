package cn.withzz.xinghuo.schedules;


import cn.withzz.crowdsourcing.base.Distribution;
import cn.withzz.xinghuo.dao.RegisterDao;
import cn.withzz.xinghuo.dao.TaskDao;
import cn.withzz.xinghuo.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskAssigner {
    @Autowired
    TaskDao taskDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    RegisterDao registerDao;
    private List<cn.withzz.xinghuo.domain.Task> tasksFilter(){
//        List<cn.withzz.xinghuo.domain.Task> tasks = taskDao.findByCondition(null);
        List<cn.withzz.xinghuo.domain.Task> tasks = taskDao.findAll();
        return tasks;
    }

    private  List<cn.withzz.crowdsourcing.base.Task>  tasksTrans (List<cn.withzz.xinghuo.domain.Task> tasks){
        //TODO
        return null;
    }

    private  List<cn.withzz.crowdsourcing.base.User>  usersTrans (List<cn.withzz.xinghuo.domain.UserInfo> users){
        //TODO
        return null;
    }

    public List<Distribution>  assign(){
        List<cn.withzz.xinghuo.domain.Task> tasks = tasksFilter();
        List<cn.withzz.crowdsourcing.base.Task> tranTasks = tasksTrans(tasks);
        for(int i= 0 ;i < tasks.size(); i++){
            cn.withzz.xinghuo.domain.Task task = tasks.get(i);
            List<String> usernames = registerDao.findByTask(task.getId());
            List<cn.withzz.xinghuo.domain.UserInfo> userInfos = new ArrayList<cn.withzz.xinghuo.domain.UserInfo>();
            for (String username: usernames) {
                userInfos.add(userInfoDao.findByKey(username));
            }
            List<cn.withzz.crowdsourcing.base.User> transUsers = usersTrans(userInfos);
            tranTasks.get(i).setRegister(transUsers);
        }
        return null;
    }

}

