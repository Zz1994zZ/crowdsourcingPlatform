package cn.withzz.xinghuo.dao;


import cn.withzz.xinghuo.domain.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 用户 DAO 接口类
 *
 * Created by svenzzhou on 08/11/2018.
 */
@Mapper // 标志为 Mybatis 的 Mapper
public interface UserDao {

    @Select("SELECT username,createTime FROM users where status = 1")
    // 返回 Map 结果集
    List<User> findAll();


    List<User> findByPage();

    @Select("SELECT * FROM users where status != 0 and username=#{username}")
    // 返回 Map 结果集
    @Results({
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "createTime", column = "createTime"),
            @Result(property = "updateTime", column = "updateTime"),
            @Result(property = "status", column = "status"),
    })
    User findByKey(@Param("username") String username);

    @Insert("insert into users values(#{username},#{password},#{createTime},#{updateTime},1)")
    Long save(User user);

    @Update("update users set password=#{password},updateTime=#{updateTime} where username=#{username} and status!=0")
    Long update(User user);

    Long delete(String username);
}
