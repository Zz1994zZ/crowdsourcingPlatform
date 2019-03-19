package cn.withzz.xinghuo.domain;


import java.sql.Timestamp;
import java.util.Date;

/**
 * 用户实体类
 * Created by svenzzhou on 08/11/2018.
 */
public class User {
    private String username;
    //密码 createTime加盐
    private String password;
    private Timestamp createTime;
    private Timestamp updateTime;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
