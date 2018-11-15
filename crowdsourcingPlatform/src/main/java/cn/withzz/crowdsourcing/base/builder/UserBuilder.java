package cn.withzz.crowdsourcing.base.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;
import cn.withzz.crowdsourcing.core.TimeRangeInt;

public class UserBuilder {
	public static List<User> createUsers(Task task) throws Exception{
		int g=task.getG();
		Random r=new Random();
		int startIndex=r.nextInt(24);
		List<User> users=new ArrayList<User>();
		for (int i = 0; i <task.getModels().size(); i++) {
			User user=new User();
			user.setSkillMap(new HashMap<String, Float>());
			user.getSkillMap().put("java", TaskBuilder.sigmod(r.nextGaussian()));
			user.setTimeRangeInt(new TimeRangeInt());
			for (int j = startIndex; j < startIndex+g; j++) {
				int index=j%24+1;
				user.getTimeRangeInt().addRange(index, index);
			}
			randomAddTime(user.getTimeRangeInt());
			users.add(user);
		}
		return users;
	}
	public static void main(String[] args)  {
	
		try {
			List<Task> tasks=TaskBuilder.createTasks(20, 5, 10, "java");
			for (Task task : tasks) {
				for (User user : createUsers(task)){
					System.out.println(user.getTimeRangeInt());
				}
				System.out.println("------------------------");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void randomAddTime(TimeRangeInt tri) throws Exception{
		int random=new Random().nextInt(8);
		int startIndex=new Random().nextInt(24);
		for (int j = startIndex; j < startIndex+random; j++) {
			int index=j%24+1;
			tri.addRange(index, index);
		}
	}
}
