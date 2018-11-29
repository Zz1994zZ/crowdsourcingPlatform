package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.dao.RegisterDao;
import cn.withzz.xinghuo.dao.TaskDao;
import cn.withzz.xinghuo.domain.Task;
import cn.withzz.xinghuo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
    public Long save(Task task) {
        task.setCreateTime(new Timestamp(new Date().getTime()));
        task.setStatus(0);
        return taskDao.save(task);
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

}
