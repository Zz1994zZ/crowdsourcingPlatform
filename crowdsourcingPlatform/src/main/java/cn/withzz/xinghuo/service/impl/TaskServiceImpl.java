package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.dao.RegisterDao;
import cn.withzz.xinghuo.dao.TaskDao;
import cn.withzz.xinghuo.domain.Task;
import cn.withzz.xinghuo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * task业务逻辑实现类
 *
 * Created by svenzzhou on 09/11/2018.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private RegisterDao registerDao;


    @Override
    public List<Task> findAll(){
        return taskDao.findAll();
    }


    @Override
    public List<Task> findByPage(int page,int per_page){
        return taskDao.findByPage((page-1)*per_page,per_page);
    }

    @Override
    public int getCount(String condition){
        return taskDao.getCount(condition);
    };


    @Override
    public Task findByKey(int id) {
        return taskDao.findByKey(id);
    }

    @Override
    public List<Task> findByCondition(String conditon) {
        return taskDao.findByCondition(conditon);
    }

    @Override
    public List<Task> findByParentTask(int parentId) {
        return taskDao.findByParentTask(parentId);
    }



    @Override
    public Long save(Task task) {
        task.setCreateTime(new Timestamp(new Date().getTime()));
        task.setStatus(1);
        return taskDao.save(task);
    }

    @Override
    public Long saveModule(Task mainTask,Task model) {
        model.setCreator(mainTask.getCreator());
        model.setCreateTime(mainTask.getCreateTime());
        model.setParentTask(mainTask.getId());
        model.setEndTime(mainTask.getEndTime());
        model.setStatus(1);
        return taskDao.save(model);
    }

    @Override
    public Long update(Task task) {
        return taskDao.update(task);
    }

    @Override
    public Long delete(int id) {
        return taskDao.delete(id);
    }

    @Override
    public List<String> getAllRegisters(int taskId) {
        return registerDao.findByTask(taskId);
    }

    @Override
    public Long register(int taskId, String username) {
        return registerDao.save(taskId,username,new Date());
    }

    @Override
    public List<Task> getPublishedTasks(String username,int status){
        List<Task> allTasks  = taskDao.findAll();
        List<Task> result = new LinkedList<Task>();
        for (Task task: allTasks) {
            if(task.getCreator().equals(username)&&(status==0||status==task.getStatus())){
                result.add(task);
            }
        }
        return result;
    }

    @Override
    public List<Task> getRegisterTasks(String username,int status){
        List<Task> allTasks  = taskDao.findAll();
        List<Integer> taskIds =  registerDao.findByUser(username);
        Map<Integer,Task> map = new HashMap<Integer,Task>();
        List<Task> result = new LinkedList<Task>();
        for (Task task: allTasks) {
            map.put(task.getId(),task);
        }
        for (int id: taskIds) {
            Task t = map.get(id);
            if(t!=null&&(status==0||status==t.getStatus())){
                result.add(map.get(id));
            }
        }
        return result;
    }

}
