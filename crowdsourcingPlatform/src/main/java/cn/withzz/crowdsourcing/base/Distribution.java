package cn.withzz.crowdsourcing.base;

import java.util.HashMap;
import java.util.List;

import cn.withzz.crowdsourcing.core.TimeRangeInt;
import cn.withzz.crowdsourcing.core.UserInfoAndRank;
import cn.withzz.crowdsourcing.core.WokerSetInt;

public class Distribution implements Comparable<Distribution>{
	private int allModelsNum;
	private Task task;
	private HashMap<Model, User> pairs;
	private float xy=0;
	private WokerSetInt wokerSetInt;
	//所在协同工作组前modelsNum效用和
	private float allModelsXYSum;
	//这个是过滤+排序的列表用于根据wokerSetInt索引到user
	List<UserInfoAndRank> tempList;
	public void show() {
		for (Model model : task.getModels()) {
			User woker = pairs.get(model);
			System.out.println(model.getComplexity() + "\t" +woker.getTimeRangeInt()+"\t" + woker.getNickname() 
					+"\t"+woker.getSkillPoint("java")
					+"\t"+computeWeight(model, woker)
					);
		}
	}
	public static Distribution create(Task task,List<User> devs){
		if(devs.size()>=task.getModels().size()){
			int i=0;
			Distribution distribution=new Distribution();
			distribution.task=task;
			distribution.pairs=new HashMap<Model, User>(task.getModels().size());
			for (Model model : task.getModels()) {
				distribution.pairs.put(model, devs.get(i));
				i++;
			}
			return distribution;
		}
		return null;
	}
	public static Distribution create(Task task,List<User> devs,WokerSetInt wokerSetInt,List<UserInfoAndRank> tempList,int allModelsNum){
		if(devs.size()>=task.getModels().size()){
			int i=0;
			Distribution distribution=new Distribution();
			distribution.allModelsNum=allModelsNum;
			distribution.wokerSetInt=wokerSetInt;
			distribution.task=task;
			distribution.tempList=tempList;
			distribution.pairs=new HashMap<Model, User>(task.getModels().size());
			for (Model model : task.getModels()) {
				distribution.pairs.put(model, devs.get(i));
				i++;
			}
			//若allModelsNum为0则跳过
			if(allModelsNum!=0)
			for (int  j : wokerSetInt.getTopNId(allModelsNum)) {
				distribution.allModelsXYSum+=tempList.get(j).getRank();
			}
			return distribution;
		}
		return null;
	}
	/**
	 * 单纯考虑能力效用时的总效用
	 * @return
	 * @throws Exception
	 */
	public float computeZXYwithoutXTYZ() throws Exception{
		float xy=0;
		TimeRangeInt timeRange=new TimeRangeInt();
		timeRange.addRange(1, 24);
		for (Model model:task.getModels()){
			timeRange=timeRange.and(pairs.get(model).getTimeRangeInt());
			xy+=model.getComplexity()*pairs.get(model).getSkillPoint(task.getSkill());
		}
		return xy/task.getModels().size();
	}
	/**
	 * 计算总效用(这里在第二篇文章中有更改——未求平均，同时除去了协同因子影响)
	 * @return
	 * @throws Exception
	 */
	public float computeZXY(){
		float xy=0;
		for (Model model:task.getModels()){
			xy+=model.getComplexity()*pairs.get(model).getSkillPoint(task.getSkill());
		}
		return xy;
	}
//	/**
//	 * 计算总效用
//	 * @return
//	 * @throws Exception
//	 */
//	public float computeZXY(){
//		float xy=0;
//		TimeRangeInt timeRange=new TimeRangeInt();
//		try {
//			timeRange.addRange(1, 24);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		for (Model model:task.getModels()){
//			timeRange=timeRange.and(pairs.get(model).getTimeRangeInt());
//			xy+=model.getComplexity()*pairs.get(model).getSkillPoint(task.getSkill());
//		}
//		return xy/task.getModels().size()*timeRange.cwc();
//	}
	public Task getTask() {
		return task;
	}
	public float getXy() {
		if(xy==0)
			xy=computeZXY();
		return xy;
	}
	public int compareTo(Distribution o) {
		//最优效用优先
		if(this.getXy()>o.getXy())
			return 1;
		//其次判断前allModelNum个工人能力和大小
		if(this.allModelsXYSum>o.allModelsXYSum)
			return 1;
		return -1;
	}
	public HashMap<Model, User> getPairs() {
		return pairs;
	}
	public WokerSetInt getWokerSetInt() {
		return wokerSetInt;
	}
	public List<UserInfoAndRank> getTempList() {
		return tempList;
	}
	public static int computeWeight(Model model, User user){
		double weight=model.getComplexity()*user.getSkillPoint(model.getTask().getSkill());
//		weight=weight*model.getTask().getG()/24;
		return (int) (weight*100000000);
//		return weight;
	}
	
}
