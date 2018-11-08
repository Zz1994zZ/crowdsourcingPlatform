package cn.withzz.xinghuo.service.impl;

import cn.withzz.xinghuo.domain.Token;
import cn.withzz.xinghuo.domain.User;
import cn.withzz.xinghuo.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 城市业务逻辑实现类
 *
 * Created by bysocket on 07/02/2017.
 */
@Service
public class TokenServiceImpl implements TokenService {

//    @Autowired
//    private TokenDao tokenDao;
    @Override
    public Long add(Token token){
        return 0L;
    };
    @Override
    public Long delete(String token){
        return 0L;
    };

}
