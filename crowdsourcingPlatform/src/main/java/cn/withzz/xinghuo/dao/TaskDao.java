package cn.withzz.xinghuo.dao;


import cn.withzz.xinghuo.domain.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 任务 DAO 接口类
 *
 * Created by svenzzhou on 11/11/2018.
 */
@Mapper // 标志为 Mybatis 的 Mapper
public interface TaskDao {

    @Select("SELECT * FROM tasks")
    // 返回 Map 结果集
    List<Task> findAll();


    List<Task> findByPage();

    @Select("SELECT * FROM tasks where id=#{id}")
    // 返回 Map 结果集
    Task findByKey(@Param("id") int id);

    @Insert("insert into tasks values(0,#{parentTask},#{name},#{type},#{creator},#{properties},#{createTime},#{endTime},#{status})")
    Long save(Task task);

    @Update("update tasks set name=#{name},executor=#{executor},properties=#{properties},endTime=#{endTime},status=#{status} where id=#{id}")
    Long update(Task task);

    Long delete(int id);
}
