package cn.withzz.xinghuo.service;


import cn.withzz.xinghuo.domain.Token;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.Set;

/**
 * 用户业务逻辑接口类
 * <p>
 * Created by svenzzhou on 08/1/2018.
 */
public interface RedisService {
    void saveToken(Token token);

    void save (String key, String value);

    void delete (String key);

    <V> V query (String key);

    <T> Set<T> getKeys (String pattern);

    RedisTemplate getRedisTemplate();

    void setRedisTemplate(RedisTemplate redisTemplate);
}
