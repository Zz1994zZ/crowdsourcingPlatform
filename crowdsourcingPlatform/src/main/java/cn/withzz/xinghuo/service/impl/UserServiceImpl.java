package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.dao.UserDao;
import cn.withzz.xinghuo.domain.User;
import cn.withzz.xinghuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 城市业务逻辑实现类
 *
 * Created by bysocket on 07/02/2017.
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
    public Long save(User User) {
        return userDao.save(User);
    }

    @Override
    public Long update(User User) {
        return userDao.update(User);
    }

    @Override
    public Long delete(Long id) {
        return userDao.delete(id);
    }

}
