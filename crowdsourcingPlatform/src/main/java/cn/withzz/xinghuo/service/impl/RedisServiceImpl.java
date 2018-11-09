package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.domain.Token;
import cn.withzz.xinghuo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by svenzzhou on 09/11/2018.
 */
@Service
public class RedisServiceImpl implements RedisService{
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void saveToken(Token token) {
        redisTemplate.opsForValue().set(token.getUsername(), token.getTokenCode(),token.getExprieTime(), TimeUnit.SECONDS);
    }

    /**
     * 保存元素对象进入缓存
     *
     * @param key 存储的Key键
     * @param value 存储的Value值
     */
    @Override
    public void save (String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 删除元素对象从缓存中
     *
     * @param key 删除的Key键
     * @return
     */
    @Override
    public void delete (String key) {
        redisTemplate.delete(key);
    }

    /**
     * 获得Key对应的值
     *
     * @param key
     */
    @Override
    public <V> V query (String key) {
        return (V)redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> Set<T> getKeys (String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
