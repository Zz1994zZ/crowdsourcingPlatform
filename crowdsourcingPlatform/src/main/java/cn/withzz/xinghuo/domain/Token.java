package cn.withzz.xinghuo.domain;


import java.util.Date;

/**
 * Token实体类
 * Created by svenzzhou on 08/11/2018.
 */
public class Token {
    private String tokenCode;
    private String username;
    private Date createTime;
    private Date updateTime;
    private int type;
    //过期时间毫秒
    private long exprieTime;

    public String getTokenCode() {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getExprieTime() {
        return exprieTime;
    }

    public void setExprieTime(long exprieTime) {
        this.exprieTime = exprieTime;
    }
}
