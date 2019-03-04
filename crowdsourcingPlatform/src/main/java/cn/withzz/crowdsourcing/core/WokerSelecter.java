package cn.withzz.crowdsourcing.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import cn.withzz.crowdsourcing.base.Model;
import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;

//单任务分配工具
public class WokerSelecter {
	private List<User> users;
	private Task task;
	//能力的影响因子
	private float alpha;
	public WokerSelecter(Task task,float alpha) {
		this.task = task;
		this.alpha = alpha;
		this.users  = task.getRegister();
	}

	public void assgin() throws Exception {
		ArrayList<User> devs=selectDevFromAll();
		TreeSet<Model> models =  task.getModels();
		int mSize = models.size();
		if(devs.size()>=mSize){
			int i =0 ;
			for(Model m:models){
				m.setWoker(devs.get(i));
				i++;
			}
		}
	}

	private ArrayList<User> selectDevFromAll() throws Exception {
		List<UserInfoAndRank> temp = UserFilter.filter(users, task);
		// System.out.println("筛选技能完毕，共" + temp.size() + "人");
		// 得到SD集合
		// ArrayList<WokerSetInt> tis = createTWD(task, temp);
		// return getSD(task.getModels().size(),tis, temp);
		return getSDoneStep(task, temp);
	}

	public ArrayList<User> selectByAbility(Task task) throws Exception {
		List<UserInfoAndRank> temp = UserFilter.filter(users, task);
		Collections.sort(temp);
		// System.out.println("筛选技能完毕，共" + temp.size() + "人");
		// 得到SD集合
		// ArrayList<WokerSetInt> tis = createTWD(task, temp);
		// return getSD(task.getModels().size(),tis, temp);
		ArrayList<User> r = new ArrayList<User>();
		for (int i = 0; i < task.getModels().size(); i++) {
			r.add(temp.get(i).getUser());
		}
		return r;
	}

	public ArrayList<User> getSDoneStep(Task task,
			List<UserInfoAndRank> users) throws Exception {
		// 先筛选用有拥有所需技能的开发人员
		WokerSetInt[] wi = new WokerSetInt[24];
		for (int i = 0; i < wi.length; i++) {
			wi[i] = new WokerSetInt(users.size());
		}
		// 让temp以评分降序排序
		Collections.sort(users);
		// System.out.println("初始化完毕");
		long startTime = System.currentTimeMillis(); // 获取开始时间
		for (int i = 0; i < users.size(); i++) {
			int[] a = users.get(i).getUser().getTimeRangeInt().toArray();
			for (int j : a) {
				wi[j].addWokerIndex(i);
			}
		}
		// 直接取能力最优解
		ArrayList<UserInfoAndRank> r = new ArrayList<UserInfoAndRank>();;
		for (int i = 0; i < task.getModels().size(); i++) {
			r.add(users.get(i));
		}
		float maxW  = computeW(r);
		for (int g = 1; g <= 8; g++) {
			CCreator cc = new CCreator(24, g);
			while (cc.hasNext()) {
				TimeRangeInt t = new TimeRangeInt(cc.next());
				int[] a = t.toArray();
				WokerSetInt ti = null;
				for (int i = 0; i < a.length; i++) {
					if (i == 0)
						ti = wi[a[i]];
					ti = ti.and(wi[a[i]]);
				}
				if (ti.abs() >= task.getModels().size()) {
					int[] wokerIndex = ti.getTopNId(task.getModels().size());
					ArrayList<UserInfoAndRank> temp=new ArrayList<UserInfoAndRank>();
					for (int i : wokerIndex) {
						temp.add(users.get(i));
					}
					float w = computeW(temp);
					if (w > maxW) {
						maxW = w;
						r = temp;
					}
				}
			}
		}

		// System.out.println(num);
		long endTime = System.currentTimeMillis(); // 获取结束时间
		// System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //
		// 输出程序运行时间
		ArrayList<User> result=new ArrayList<User>();
		for (UserInfoAndRank user : r) {
			result.add(user.getUser());
		}
		return result;
	}

	public  float computeW(ArrayList<UserInfoAndRank> users) {
		float xy = 0;
		float ea;
		int tl;
		int cs;
		float L;
		float C;
		float w;
		TimeRangeInt tri = new TimeRangeInt((1 << 24) - 1);
		for (UserInfoAndRank user : users) {
			xy += user.getRank();
			tri = tri.and(user.getUser().getTimeRangeInt());
		}
		ea = xy / users.size();
		tl = tri.abs();
		cs = tri.ctp();
		L= (float) (1 / (1 + Math.exp(8-tl)));
		C = (float) Math.exp(0.1*(1 - cs));
		w = alpha * ea + (1 - alpha) * (L * C);
		return w;
	}

	public ArrayList<User> getSD(int n, ArrayList<WokerSetInt> tis,
			ArrayList<UserInfoAndRank> temp) {
		ArrayList<User> r = new ArrayList<User>();
		float maxXY = 0;
		for (WokerSetInt ti : tis) {
			float xy = 0;
			int[] wokerIndex = ti.getTopNId(n);
			for (int i : wokerIndex) {
				xy += temp.get(i).getRank();
			}
			if (xy > maxXY) {
				maxXY = xy;
				r = new ArrayList<User>();
				for (int i : wokerIndex) {
					r.add(temp.get(i).getUser());
				}
			}
		}
		System.out.println(maxXY);
		return r;
	}

	public ArrayList<WokerSetInt> createTWD(Task task,
			ArrayList<UserInfoAndRank> users) throws Exception {
		// 先筛选用有拥有所需技能的开发人员
		WokerSetInt[] wi = new WokerSetInt[24];
		for (int i = 0; i < wi.length; i++) {
			wi[i] = new WokerSetInt(users.size());
		}
		// 让temp以评分降序排序
		Collections.sort(users);
		System.out.println("初始化完毕");
		long startTime = System.currentTimeMillis(); // 获取开始时间
		for (int i = 0; i < users.size(); i++) {
			int[] a = users.get(i).getUser().getTimeRangeInt().toArray();
			for (int j : a) {
				wi[j].addWokerIndex(i);
			}
		}
		// File file = new File("zz-test.txt");
		// FileOutputStream fos = new FileOutputStream(file);
		// PrintStream ps = new PrintStream(fos);
		ArrayList<WokerSetInt> tis = new ArrayList<WokerSetInt>();
		CCreator cc = new CCreator(24, task.getG());
		while (cc.hasNext()) {
			TimeRangeInt t = new TimeRangeInt(cc.next());
			int[] a = t.toArray();
			WokerSetInt ti = null;
			for (int i = 0; i < a.length; i++) {
				if (i == 0)
					ti = wi[a[i]];
				ti = ti.and(wi[a[i]]);
			}
			if (ti.abs() >= task.getModels().size()) {
				// ps.println(t + "mode:" + ti.abs());
				// for (int i : ti.toWokerIndexArray()) {
				// ps.println(users.get(i).getUser());
				// }
				// ps.println(ti.toString());
				tis.add(ti);
			}
		}
		// ps.flush();
		// fos.flush();
		// ps.close();
		// fos.close();
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间
		return tis;
	}
}
