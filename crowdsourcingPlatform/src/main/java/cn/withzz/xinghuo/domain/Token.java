package cn.withzz.xinghuo.domain;


import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Token实体类
 * Created by svenzzhou on 08/11/2018.
 */
public class Token {
    private String tokenCode;
    private String username;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Tpye type;
    //过期时间秒
    private long exprieTime;

    public static enum Tpye {USER,OTHER};
    public String getTokenCode() {
        if(tokenCode == null)
            this.generateToken();
        return tokenCode;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Tpye getType() {
        return type;
    }

    public void setType(Tpye type) {
        this.type = type;
    }

    public long getExprieTime() {
        return exprieTime;
    }

    public void setExprieTime(long exprieTime) {
        this.exprieTime = exprieTime;
    }

    private void generateToken(){
        StringBuffer sb =new StringBuffer();
        sb.append(username);
        String temp = createTime+new Date().toString();
        sb.append(DigestUtils.md5DigestAsHex(temp.getBytes()));
        this.tokenCode=sb.toString();
    }
}
