package cn.withzz.xinghuo.domain;


import java.sql.Timestamp;

/**
 * 用户信息实体类
 * Created by svenzzhou on 12/11/2018.
 */
public class UserInfo {
    private String username;
    private String name;
    //json data
    private String skillList;
    private String info;
    private String extention;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkillList() {
        return skillList;
    }

    public void setSkillList(String skillList) {
        this.skillList = skillList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
