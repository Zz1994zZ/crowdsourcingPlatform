package cn.withzz.xinghuo.domain;


import java.util.Date;

/**
 * 用户实体类
 * Created by svenzzhou on 08/11/2018.
 */
public class User {
    private String username;
    //密码 createTime加盐
    private String password;
    private Date createTime;
    private Date updateTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
