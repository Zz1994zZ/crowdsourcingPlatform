package cn.withzz.xinghuo.service;


import cn.withzz.xinghuo.domain.User;

import java.util.List;

/**
 * 用户业务逻辑接口类
 * <p>
 * Created by svenzzhou on 08/1/2018.
 */
public interface UserService {

    List<User> findAll();

    User findByKey(String username);

    Long save(User user);

    Long update(User user);

    Long delete(String username);
}
