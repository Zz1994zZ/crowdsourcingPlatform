package cn.withzz.xinghuo.dao;


import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 报名表 DAO 接口类
 *
 * Created by svenzzhou on 15/11/2018.
 */
@Mapper // 标志为 Mybatis 的 Mapper
public interface RegisterDao {

//    @Select("SELECT * FROM taskregister")
    // 返回 Map 结果集
//    List<> findAll();
//
//
//    List<> findByPage();

    @Select("SELECT username FROM taskregister where taskId=#{taskId}")
    // 返回 Map 结果集
    List<String> findByTask(@Param("taskId") int taskId);

    @Select("SELECT taskId FROM taskregister where username=#{username}")
        // 返回 Map 结果集
    List<Integer> findByUser(@Param("username") String username);


    @Insert("insert into taskregister values(#{taskId},#{username},#{createTime},1)")
    Long save(@Param("taskId") int taskId, @Param("username")String username, @Param("createTime")Date createTime);

    @Update("update taskregister set status=0 where taskId=#{taskId} and username=#{username}")
    Long close(@Param("taskId")int taskId, @Param("username")String username);

    Long delete(int id);
}
