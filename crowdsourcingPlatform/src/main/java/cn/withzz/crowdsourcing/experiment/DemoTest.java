package cn.withzz.crowdsourcing.experiment;

import java.util.*;

import cn.withzz.crowdsourcing.base.Distribution;
import cn.withzz.crowdsourcing.base.Model;
import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;
import cn.withzz.crowdsourcing.core.CoCGroup;
import cn.withzz.crowdsourcing.core.KMimplement;
import cn.withzz.crowdsourcing.core.TimeRangeInt;
import cn.withzz.crowdsourcing.core.WorkAssigner;

public class DemoTest {
	public static void main(String[] args) throws Exception {
		User d1=new User();
		d1.setNickname("d1");
		d1.setSkillMap(new HashMap<String, Float>());
		d1.getSkillMap().put("java", 0.8f);
		d1.setTimeRangeInt(new TimeRangeInt("000000001111001111000000"));
		User d2=new User();
		d2.setNickname("d2");
		d2.setSkillMap(new HashMap<String, Float>());
		d2.getSkillMap().put("java", 0.7f);
		d2.setTimeRangeInt(new TimeRangeInt("000000000000001111000000"));
		User d3=new User();
		d3.setNickname("d3");
		d3.setSkillMap(new HashMap<String, Float>());
		d3.getSkillMap().put("java", 0.6f);
		d3.setTimeRangeInt(new TimeRangeInt("000000001111000000000000"));
		User d4=new User();
		d4.setNickname("d4");
		d4.setSkillMap(new HashMap<String, Float>());
		d4.getSkillMap().put("java", 0.5f);
		d4.setTimeRangeInt(new TimeRangeInt("000000000000000111110000"));
		User d5=new User();
		d5.setNickname("d5");
		d5.setSkillMap(new HashMap<String, Float>());
		d5.getSkillMap().put("java", 0.5f);
		d5.setTimeRangeInt(new TimeRangeInt("111111000000000000000001"));
		User d6=new User();
		d6.setNickname("d6");
		d6.setSkillMap(new HashMap<String, Float>());
		d6.getSkillMap().put("java", 0.5f);
		d6.setTimeRangeInt(new TimeRangeInt("111000000000000000000000"));
		User d7=new User();
		d7.setNickname("d7");
		d7.setSkillMap(new HashMap<String, Float>());
		d7.getSkillMap().put("java", 0.5f);
		d7.setTimeRangeInt(new TimeRangeInt("111111111111111111111111"));
		User d8=new User();
		d8.setNickname("d8");
		d8.setSkillMap(new HashMap<String, Float>());
		d8.getSkillMap().put("java", 0.4f);
		d8.setTimeRangeInt(new TimeRangeInt("000000000000000000111100"));
		User d9=new User();
		d9.setNickname("d9");
		d9.setSkillMap(new HashMap<String, Float>());
		d9.getSkillMap().put("java", 0.3f);
		d9.setTimeRangeInt(new TimeRangeInt("000000111110000000000000"));
		User d10=new User();
		d10.setNickname("d10");
		d10.setSkillMap(new HashMap<String, Float>());
		d10.getSkillMap().put("java", 0.2f);
		d10.setTimeRangeInt(new TimeRangeInt("000000000011111100000000"));
		
		List<User> users=new ArrayList<User>();
		users.add(d1);
		users.add(d2);
		users.add(d3);
		users.add(d4);
		users.add(d5);
		users.add(d6);
		users.add(d7);
		users.add(d8);
		users.add(d9);
		users.add(d10);
		
		
		Task t1=new Task();
		t1.setId(1);
		t1.setG(2);
		t1.setSkill("java");
		t1.getModels().add(new Model(0.7f,"java",t1));
		t1.getModels().add(new Model(0.6f,"java",t1));
		t1.getModels().add(new Model(0.6f,"java",t1));
		t1.setRegister(users);

		Task t2=new Task();
		t2.setId(2);
		t2.setG(3);
		t2.setSkill("java");
		t2.getModels().add(new Model(0.8f,"java",t2));
		t2.getModels().add(new Model(0.4f,"java",t2));
		t2.setRegister(users);
		
		Task t3=new Task();
		t3.setId(3);
		t3.setG(2);
		t3.setSkill("java");
		t3.getModels().add(new Model(0.5f,"java",t3));
		t3.getModels().add(new Model(0.4f,"java",t3));
		t3.getModels().add(new Model(0.4f,"java",t3));
		t3.setRegister(users);
		
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);
		
		
		System.out.println("开始匹配---------------------------------");
//		for (Task task : tasks) {
//			System.out.println(task.getModels());
//			System.out.println(task.getRegister());
//			//System.out.println("任务g=" + task.getG()+",mnum="+task.getModels().size());
//			PriorityQueue<CoCGroup> ccgPQ=WorkAssigner.buildCoCGroups(task, task.getRegister(),100);
//			while(!ccgPQ.isEmpty()){
//				ccgPQ.poll().show();
//			}
//			System.out.println("---------------------------------");
//		}
		//本文算法
//		float zxy1 = 0;
		try{
			System.out.println("KM匹配---------------------------------");
			for (Distribution distribution : new KMimplement(tasks,false).KM()) {
//				 System.out.println("总体优化任务g="+distribution.getTask().getG());
//				 distribution.show();
				HashMap<Model, User> map  = distribution.getPairs();
				for(Model model:map.keySet()){
					model.setWorker(map.get(model));
				}
				Task task = distribution.getTask();
				System.out.println(task.getId()+"\t"+task.getG());
				task.showDevs();
//				 System.out.println("分配效用"+distribution.getXy());
//				zxy1 += distribution.getXy();
			}
//			System.out.println("KM总效用：" + zxy1);
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
}
