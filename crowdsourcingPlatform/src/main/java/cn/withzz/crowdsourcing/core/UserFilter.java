package cn.withzz.crowdsourcing.core;

import java.util.ArrayList;
import java.util.List;

import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;

public class UserFilter {
	public static List<UserInfoAndRank> filter(List<User> users,Task task){
		return filter(users, task.getSkill().toLowerCase());
	}
	public static List<UserInfoAndRank> filter(List<User> users,String skill){
		ArrayList<UserInfoAndRank> temp = new ArrayList<UserInfoAndRank>();
		for (User user : users) {
			if(user.getSkillMap().containsKey(skill)){
				temp.add(new UserInfoAndRank(user, user.getSkillMap().get(skill)));
			}
		}
		return temp;
	}
}
