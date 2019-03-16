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

    @Select("SELECT * FROM tasks where parentTask = 0")
    // 返回 Map 结果集
    List<Task> findAll();

    @Select("SELECT * FROM tasks where parentTask = #{id}")
        // 返回 Map 结果集
    List<Task> findByParentTask(@Param("id") int id);

    @Select("select * from tasks where parentTask = 0 and status = 1 order by createTime desc limit #{start}, #{pageSize}")
    List<Task> findByPage(@Param("start") int start,@Param("pageSize") int per_page);

    @Select("select count(id) from tasks where parentTask = 0 and status = 1")
//    @Select("<script>"
//            + "SELECT COUNT(*) "
//            + "FROM tasks "
//            + "<if test='condition!=null'>"
//            + "WHERE #{condition}"
//            + "</if>"
//            + "</script>")
    int getCount(@Param("condition") String condition);

    @Select("<script>"
            + "SELECT * "
            + "FROM tasks "
            + "<if test='condition!=null'>"
            + "WHERE #{condition}"
            + "</if>"
            + "</script>")
    List<Task> findByCondition(@Param("condition") String condition);


    @Select("SELECT * FROM tasks where id=#{id}")
    // 返回 Map 结果集
    Task findByKey(@Param("id") int id);

    @Insert("insert into tasks values(0,#{parentTask},#{name},#{type},#{creator},#{executor},#{properties},#{createTime},#{endTime},#{status})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    Long save(Task task);

    @Update("update tasks set name=#{name},executor=#{executor},properties=#{properties},endTime=#{endTime},status=#{status} where id=#{id}")
    Long update(Task task);

    Long delete(int id);
}
