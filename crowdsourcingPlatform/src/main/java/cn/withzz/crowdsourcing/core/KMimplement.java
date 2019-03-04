package cn.withzz.crowdsourcing.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.Set;

import cn.withzz.crowdsourcing.base.Distribution;
import cn.withzz.crowdsourcing.base.Model;
import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;
import cn.withzz.crowdsourcing.core.LinkInfo.Type;


//多任务分配工具
public class KMimplement {
	private HashMap<Model, LinkInfo> modelsMap = new HashMap<Model, LinkInfo>();
	private HashMap<User, LinkInfo> usersMap = new HashMap<User, LinkInfo>();
	private List<Task> tasks;
	private HashMap<Task, List<User>> candidates = new HashMap<Task, List<User>>();
	static int inf = Integer.MAX_VALUE;
	boolean usex[], usey[];// 本回合使用的x，y
	private HashSet<Model> usedModel;
	private HashSet<User> usedUser;
	private boolean log = false;

	public KMimplement(List<Task> tasks, boolean log) {
		this.tasks = tasks;
		this.log = log;
		for (Task task : tasks) {
			// 初始化模块的LinkInfo
			for (Model model : task.getModels()) {
				modelsMap.put(model, new LinkInfo(Type.X));
			}
			// 初始化工人的LinkInfo
			for (User user : task.getRegister()) {
				// 这里可能在之前的任务注册工人中初始化过，所以判断一下
				if (usersMap.get(user) == null)
					usersMap.put(user, new LinkInfo(Type.Y));
			}
		}
	}

	// 初始化标杆
	private void init() throws Exception {
		if (log)
			System.out.println("开始初始化");
		for (Task task : tasks) {
			if (log)
				System.out.println("任务g=" + task.getG() + ",mnum="
						+ task.getModels().size());
			// 这里直接得到了单任务最优的一个协同工作组
			Distribution bestDistribution = WorkAssigner.SingleTaskAssigne(
					task, task.getRegister(), 1, modelsMap.size()).peek();
			// 打印最优解
			bestDistribution.show();
			for (Entry<Model, User> entry : bestDistribution.getPairs()
					.entrySet()) {
				Model model = entry.getKey();
				User user = entry.getValue();
				modelsMap.get(entry.getKey()).setDb(
						Distribution.computeWeight(model, user));
			}
			List<User> users = new ArrayList<User>();
			for (int i : bestDistribution.getWokerSetInt().toWokerIndexArray()) {
				users.add(bestDistribution.getTempList().get(i).getUser());
			}
			for (int i = 0; i < task.getModels().size(); i++) {
				System.out.println(users.get(i));
			}
			candidates.put(task, users);
		}
		if (log)
			System.out.println("初始化完毕");
	}

	// 初始化标杆
	private void init2() throws Exception {
		if (log)
			System.out.println("开始初始化");
		for (Task task : tasks) {
			if (log)
				System.out.println("任务g=" + task.getG() + ",mnum="
						+ task.getModels().size());
			// 这里直接得到了单任务最优的一个协同工作组
			Distribution bestDistribution = WorkAssigner.SingleTaskAssigne(
					task, task.getRegister()).peek();
			// 打印最优解
			bestDistribution.show();
			for (Model model : task.getModels()) {
				modelsMap.get(model).setDb(
						Distribution.computeWeight(model, bestDistribution
								.getTempList().get(0).getUser()));
			}
			List<User> users = new ArrayList<User>();
			for (int i : bestDistribution.getWokerSetInt().toWokerIndexArray()) {
				users.add(bestDistribution.getTempList().get(i).getUser());
			}
			for (int i = 0; i < task.getModels().size(); i++) {
				System.out.println(users.get(i));
			}
			candidates.put(task, users);
		}
		if (log)
			System.out.println("初始化完毕");
	}

	// 初始化标杆
	private void init3() throws Exception {
		if (log)
			System.out.println("开始初始化");
		for (Task task : tasks) {
			// System.out.println("初始化任务g=" + task.getG() + ",mnum="
			// + task.getModels().size());
			// 先找前10000个最优ccg再说
			PriorityQueue<CoCGroup> ccgPQ = WorkAssigner.buildCoCGroups(task,
					task.getRegister(), 10000);
			// 把ccg放到链表里面头插保证最大在最前面（因为最小堆pop每次最小）
			LinkedList<CoCGroup> ccgs = new LinkedList<CoCGroup>();
			while (!ccgPQ.isEmpty()) {
				ccgs.addFirst(ccgPQ.poll());
			}
			// 装配一下ccgs
			task.setCcgs(ccgs);
			// 初始设置使用最优ccg
			replace2NextCoCG(task);
		}
		if (log)
			System.out.println("初始化完毕");
	}

	// 替换ccg，主要是要修改顶标
	private void replace2NextCoCG(Task task) {
		CoCGroup group = task.getCcgs().pollFirst();
		task.setCcg(group);
		List<User> users = group.getUsers();
		User bestOne = users.get(0);
		int bestUDB = usersMap.get(bestOne).getDb();
		for (Model model : task.getModels()) {
			// 初始化相关边
			Object o = modelsMap.get(model).getLink();
			if (o != null) {
				usersMap.get(o).setLink(null);
			}
			modelsMap.get(model).setLink(null);
			// 建立与最优选的边
			modelsMap.get(model).setDb(
					Distribution.computeWeight(model, users.get(0)) - bestUDB);
		}
		candidates.put(task, group.getUsers());
	}

	// 确定替换ccg的任务
	private Task findTask2replaceCCG(Set<Task> tasks) {
		Task task = null;
		float cgap = Float.MAX_VALUE;
		for (Task t : tasks) {
			CoCGroup g = t.getCcgs().peekFirst();
			if (g == null)
				continue;
			CoCGroup current = t.getCcg();
			float gap = current.getMinXY() - g.getMinXY();
			if (gap < cgap) {
				cgap = gap;
				task = t;
			}
		}
		return task;
	}

	// 深搜找增广路径
	boolean dfs(Model model) {
		usedModel.add(model);
		LinkInfo modelLinkInfo = modelsMap.get(model);
		for (User user : candidates.get(model.getTask())) {
			LinkInfo userLinkInfo = usersMap.get(user);
			if (!usedUser.contains(user)
					&& modelLinkInfo.getDb() + userLinkInfo.getDb() == Distribution
							.computeWeight(model, user)) {
				usedUser.add(user);
				if (userLinkInfo.getLink() == null
						|| dfs((Model) userLinkInfo.getLink())) {
					userLinkInfo.setLink(model);
					modelLinkInfo.setLink(user);
					return true;
				}
			}
		}
		return false;
	}

	// 主流程
	public List<Distribution> KM() {
		// 初始化
		try {
			init3();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int n = 0;
		boolean replaceCCG = false;
		while (true) {
			n = 0;
			replaceCCG = false;
			for (Entry<Model, LinkInfo> entry : modelsMap.entrySet()) {
				Model model = entry.getKey();
				if (log)
					System.out.println("find for:" + n + "-" + model);
				n++;
				if (modelsMap.get(model).getLink() != null)
					continue;
				while (true) {
					int d = inf;
					usedModel = new HashSet<Model>();
					usedUser = new HashSet<User>();
					if (dfs(model))
						break;
					HashSet<Task> usedTasks = new HashSet<Task>();
					for (Model udmodel : usedModel) {
						usedTasks.add(udmodel.getTask());
						// 遍历报名用户寻找udmodel次优边
						for (User user : candidates.get(udmodel.getTask())) {
							if (!usedUser.contains(user))
								d = Math.min(
										d,
										getDB(udmodel)
												+ getDB(user)
												- Distribution.computeWeight(
														udmodel, user));
						}
					}
					if (d == inf) {
						if (log)
							System.out.println("可能提前终止于" + model + "让我们挽救一波~");
						// 哈哈 这里需要一个goto一样的功能~
						Task t = findTask2replaceCCG(usedTasks);
						if (t != null) {
							replace2NextCoCG(t);
							replaceCCG = true;
							break;
						}
						return null;
					}
					for (Model udmodel : usedModel) {
						LinkInfo ml = modelsMap.get(udmodel);
						ml.setDb(ml.getDb() - d);
					}
					for (User uduser : usedUser) {
						LinkInfo ul = usersMap.get(uduser);
						ul.setDb(ul.getDb() + d);
					}
				}
				if (replaceCCG)
					break;
			}
			if (!replaceCCG)
				break;
		}
		List<Distribution> list = new ArrayList<Distribution>();
		for (Task task : tasks) {
			List<User> devs = new ArrayList<User>();
			for (Model model : task.getModels()) {
				// 找到model对应工人加入分配list
				devs.add((User) modelsMap.get(model).getLink());
			}
			list.add(Distribution.create(task, devs, null, null, 0));
		}
		return list;
	}

	private int getDB(Model model) {
		return modelsMap.get(model).getDb();
	}

	private int getDB(User user) {
		return usersMap.get(user).getDb();
	}
}

class LinkInfo {
	public static enum Type {
		X, Y
	};

	private Type type;
	private int db;
	private Object Link;

	public LinkInfo(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	public Object getLink() {
		return Link;
	}

	public void setLink(Object link) {
		Link = link;
	}
}