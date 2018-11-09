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

    @Select("SELECT * FROM users where status = 1")
    // 返回 Map 结果集
    @Results({
            @Result(property = "username", column = "username"),
    })
    List<User> findAll();


    List<User> findByPage();

    @Select("SELECT * FROM users where status = 1 and username=#{username}")
    // 返回 Map 结果集
    @Results({
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "createTime", column = "createTime"),
            @Result(property = "updateTime", column = "updateTime"),
    })
    User findByKey(@Param("username") String username);

    @Insert("insert into users values(#{username},#{password},#{createTime},#{updateTime},1)")
    Long save(User user);

    @Update("update users set password=#{password},updateTime=#{updateTime} where username=#{username} and status=1")
    Long update(User user);


    Long delete(Long id);
}
