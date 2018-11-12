package cn.withzz.xinghuo.dao;


import cn.withzz.xinghuo.domain.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户信息 DAO 接口类
 *
 * Created by svenzzhou on 12/11/2018.
 */
@Mapper // 标志为 Mybatis 的 Mapper
public interface UserInfoDao {

    @Select("SELECT * FROM usersinfo")
    // 返回 Map 结果集
    List<UserInfo> findAll();


    List<UserInfo> findByPage();

    @Select("SELECT * FROM usersinfo where  username=#{username}")
    // 返回 Map 结果集
    UserInfo findByKey(@Param("username") String username);

    @Insert("insert into usersinfo values(#{username},#{name},#{skillList},#{info},#{extention})")
    Long save(UserInfo userInfo);

    @Update("update users set name=#{name},skillList=#{skillList},info=#{info},extention=#{extention} where username=#{username}")
    Long update(UserInfo userInfo);


    Long delete(String username);
}
