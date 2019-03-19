package cn.withzz.xinghuo.service;



import cn.withzz.xinghuo.domain.Task;

import java.util.List;

/**
 * 用户业务逻辑接口类
 * <p>
 * Created by svenzzhou on 14/11/2018.
 */
public interface TaskService {

    List<Task> findAll();

    List<Task> findByPage(int page,int per_page);

    int getCount(String condition);

    Task findByKey(int id);

    List<Task> findByCondition(String conditon);

    List<Task> findByParentTask(int parentId);

    Long save(Task userInfo);

    Long saveModule(Task mainTask,Task model);

    Long update(Task userInfo);

    Long delete(int id);

    List<String> getAllRegisters(int taskId);

    Long register(int taskId,String username);

    List<Task> getRegisterTasks(String username,int status);

    List<Task> getPublishedTasks(String username,int status);

    List<Task> getTaskByExcutor(String executor,int status);

    List<Task> getTaskByStatus(int status);
}
