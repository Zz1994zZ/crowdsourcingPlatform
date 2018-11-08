package cn.withzz.xinghuo.service;


import cn.withzz.xinghuo.domain.Token;
import cn.withzz.xinghuo.domain.User;

import java.util.List;

/**
 * 业务逻辑接口类
 * <p>
 * Created by svenzzhou on 08/1/2018.
 */
public interface TokenService {


    Long add(Token token);

    Long delete(String token);
}
