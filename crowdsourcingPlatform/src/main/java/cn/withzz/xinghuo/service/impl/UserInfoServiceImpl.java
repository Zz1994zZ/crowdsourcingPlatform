package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.dao.UserInfoDao;
import cn.withzz.xinghuo.domain.UserInfo;
import cn.withzz.xinghuo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.List;

/**
 * 城市业务逻辑实现类
 *
 * Created by svenzzhou on 09/11/2018.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao UserInfoDao;

    @Override
    public List<UserInfo> findAll(){
        return UserInfoDao.findAll();
    }

    @Override
    public UserInfo findByKey(String username) {
        return UserInfoDao.findByKey(username);
    }

    @Override
    public Long save(UserInfo userInfo) {
        return UserInfoDao.save(userInfo);
    }

    @Override
    public Long update(UserInfo userInfo) {
        return UserInfoDao.update(userInfo);
    }

    @Override
    public Long delete(String username) {
        return UserInfoDao.delete(username);
    }

}
