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

    Task findByKey(int id);

    Long save(Task userInfo);

    Long update(Task userInfo);

    Long delete(int id);

    List<String> getAllRegisters(int taskId);

    Long register(int taskId,String username);

}