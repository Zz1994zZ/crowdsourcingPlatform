package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.dao.UserDao;
import cn.withzz.xinghuo.domain.User;
import cn.withzz.xinghuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 城市业务逻辑实现类
 *
 * Created by svenzzhou on 09/11/2018.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAll(){
        return userDao.findAll();
    }

    @Override
    public User findByKey(String username) {
        return userDao.findByKey(username);
    }

    @Override
    public Long save(User user) {
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setUpdateTime(user.getCreateTime());
        //createTime做盐
        String salt = user.getCreateTime().getTime()+"";
        String password = user.getPassword()+salt;
        String encodeStr = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(encodeStr);
        return userDao.save(user);
    }

    @Override
    public Long update(User user) {
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        String salt = user.getCreateTime().getTime()+"";
        String password = user.getPassword()+salt;
        String encodeStr = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(encodeStr);
        return userDao.update(user);
    }

    @Override
    public Long delete(String username) {
        return userDao.delete(username);
    }

}
