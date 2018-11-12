package cn.withzz.xinghuo.service;


import cn.withzz.xinghuo.domain.UserInfo;

import java.util.List;

/**
 * 用户业务逻辑接口类
 * <p>
 * Created by svenzzhou on 08/1/2018.
 */
public interface UserInfoService {

    List<UserInfo> findAll();

    UserInfo findByKey(String username);

    Long save(UserInfo userInfo);

    Long update(UserInfo userInfo);

    Long delete(String username);
}
