package cn.withzz.crowdsourcing.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import cn.withzz.crowdsourcing.base.Distribution;
import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;
/**
 * 各种分配方法（很关键）
 * @author shmily_zz
 *
 */
public class WorkAssigner {
	/**
	 * 为任务构造协同候选组集合
	 * @param task
	 * @param users
	 * @param k
	 * @return
	 * @throws Exception
	 */
	public static PriorityQueue<CoCGroup> buildCoCGroups(Task task,List<User> users,int k) throws Exception{
		// 先筛选用有拥有所需技能的开发人员
		List<UserInfoAndRank> temp = UserFilter.filter(users, task);
		// 让temp以评分降序排序
		Collections.sort(temp);
		WokerSetInt[] OTDS=createOTD(temp);
		CCreator cc = new CCreator(24, task.getG());
		//建立小根堆
		PriorityQueue <CoCGroup> minHeap = new PriorityQueue<CoCGroup>(k);
		//遍历所有时段组合
		while (cc.hasNext()) {
			TimeRangeInt t = new TimeRangeInt(cc.next());
			int[] a = t.toArray();
			WokerSetInt ti = null;
			//得到该种组合的协同工作组ti（二进制表示的工人集合）
			for (int i = 0; i < a.length; i++) {
				if (i == 0)
					ti = OTDS[a[i]];
				ti = ti.and(OTDS[a[i]]);
			}
			//协同工作组人数大于模块数才尝试产生匹配
			if (ti.abs() >= task.getModels().size()) {
				for (int i = task.getModels().size(); i <=ti.abs(); i++) {
					//生成这个协同候选组
					CoCGroup ccg=new CoCGroup(task, ti, temp, i);
					//保留总效用前k大的协同候选组
					//如果小根堆中协同候选组数小于k直接加入
					if(minHeap.size()<k){
						if(repeatCheck(minHeap, ccg))
							minHeap.add(ccg);
					}
					else{//小根堆满时若新组大于堆中最小组则弹出最小组加入新组
						if(minHeap.peek().getMinXY()<ccg.getMinXY()){
							if(repeatCheck(minHeap, ccg)){
								minHeap.poll();
								minHeap.add(ccg);
							}
						}else//如果没当前第k小的大那么后面的必然更小，直接跳到下一个循环
							break;
					}
				}
			}
		}
		return 	minHeap;
	}
	/**
	 * 返回true说明通过重复检查
	 * @param minHeap
	 * @param ccg
	 * @return
	 */
	private static boolean repeatCheck(PriorityQueue <CoCGroup> minHeap,CoCGroup ccg){
		for (CoCGroup g: minHeap) {
			if(g.equals(ccg))
				return false;
		}
		return true;
	}
	/**
	 * 这将返回一个最小堆 里面是前k个最大的分配
	 * @param task
	 * @param users
	 * @param k
	 * @return
	 * @throws Exception
	 */
	public static PriorityQueue<Distribution> SingleTaskAssigne(Task task,List<User> users,int k,int allModelsNum) throws Exception{
		// 先筛选用有拥有所需技能的开发人员
		List<UserInfoAndRank> temp = UserFilter.filter(users, task);
		// 让temp以评分降序排序
		Collections.sort(temp);
		WokerSetInt[] OTDS=createOTD(temp);
		CCreator cc = new CCreator(24, task.getG());
		//建立小根堆
		PriorityQueue <Distribution> minHeap = new PriorityQueue<Distribution>(k);
		//遍历所有时段组合
		while (cc.hasNext()) {
			TimeRangeInt t = new TimeRangeInt(cc.next());
			int[] a = t.toArray();
			WokerSetInt ti = null;
			//得到该种组合的协同工作组ti（二进制表示的工人集合）
			for (int i = 0; i < a.length; i++) {
				if (i == 0)
					ti = OTDS[a[i]];
				ti = ti.and(OTDS[a[i]]);
			}
			//协同工作组人数大于模块数才尝试产生匹配
			if (ti.abs() >= task.getModels().size()) {
				//从ti中提取出User的下标（根据排序不等式取前|M|个最优）
				int [] wokerIndex=ti.getTopNId(task.getModels().size());
				ArrayList<User> list=new ArrayList<User>();
				//将特定下标的User加入列表
				for (int i : wokerIndex) {
					list.add(temp.get(i).getUser());
				}
				//生成这个匹配
				Distribution cg=Distribution.create(task, list,ti,temp,allModelsNum);
				//保留总效用前k大的匹配
				//如果小根堆中匹配数小于k直接加入
				if(minHeap.size()<k)
					minHeap.add(cg);
				else{//小根堆满时若新匹配大于堆中最小匹配则弹出最小匹配加入新匹配
					if(minHeap.peek().getXy()<cg.getXy()){
						minHeap.poll();
						minHeap.add(cg);
					}
				}
			}
		}
		return 	minHeap;
	}
	/**
	 * 这将返回最大的分配
	 * @param task
	 * @param users
	 * @param k
	 * @return
	 * @throws Exception
	 */
	public static PriorityQueue<Distribution> SingleTaskAssigne(Task task,List<User> users) throws Exception{
		return 	SingleTaskAssigne(task, users, 1, 0);
	}
	/**
	 * 生成 24 个代表不同时段的工人集合，每个集合包含活跃时间包括其所代表时段 的所有工人
	 * @param  UserInfoAndRank users
	 * @return
	 * @throws Exception
	 */
	private static WokerSetInt[] createOTD(List<UserInfoAndRank> temp) throws Exception{
		WokerSetInt[] wi = new WokerSetInt[24];
		for (int i = 0; i < wi.length; i++) {
			wi[i] = new WokerSetInt(temp.size());
		}
		for (int i = 0; i < temp.size(); i++) {
			int[] a = temp.get(i).getUser().getTimeRangeInt().toArray();
			for (int j : a) {
				wi[j].addWokerIndex(i);
			}
		}
		return wi;
	}
}
