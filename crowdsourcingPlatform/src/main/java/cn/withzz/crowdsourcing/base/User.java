package cn.withzz.crowdsourcing.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.withzz.crowdsourcing.core.TimeRangeInt;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6147405052869942659L;
	private String id;
	private String nickname;
	private String workTime;
	private String skillList;

	private TimeRangeInt timeRangeInt;
	private HashMap<String, Float> skillMap;
	private HashMap<String, Float> realSkillMap;
	private HashMap<String, Float> maxAMap;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getSkillList() {
		return skillList;
	}

	public void setSkillList(String skillList) {
		this.skillList = skillList;
	}

	public TimeRangeInt getTimeRangeInt() {
		if (this.timeRangeInt == null) {
			this.timeRangeInt = new TimeRangeInt();
			try {
				if (this.workTime.contains("����")) {
					this.timeRangeInt.addRange(9, 18);
				} else if (this.workTime.contains("����")) {
					this.timeRangeInt.addRange(19, 24);
				} else {
					// ��ָ��ģʽ���ַ�������
					String pattern = "((\\d+)(:))";
					// ���� Pattern ����
					Pattern r = Pattern.compile(pattern);
					// ���ڴ��� matcher ����
					Matcher mat = r.matcher(this.workTime);
					int[] timePoint = new int[4];
					int i = 0;
					while (mat.find()) {
						timePoint[i] = Integer.valueOf(mat.group(2));
						i++;
					}
					this.timeRangeInt.addTimeRange(timePoint[0], timePoint[1]);
				}
			} catch (Exception e) {
				System.out.println(this.id);
				e.printStackTrace();
			}

		}
		return timeRangeInt;
	}

	public void setTimeRangeInt(TimeRangeInt timeRangeInt) {
		this.timeRangeInt = timeRangeInt;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Float> getSkillMap() {
//		if (this.skillMap == null) {
//			HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
//			this.skillMap=new HashMap<String, Float>();
//			try{
//				temp = GSONUtil.gson.fromJson(skillList,
//						temp.getClass());
//				Set<Entry<String, ArrayList<String>>>  keys=temp.entrySet();
//
//					for(Entry<String, ArrayList<String>> entry:keys){
//						String pattern = "\\d";
//						// ���� Pattern ����
//						Pattern r = Pattern.compile(pattern);
//						// ���ڴ��� matcher ����
//						Matcher mat = r.matcher(entry.getValue().get(0));
//						if(mat.find()){
//							int level=Integer.valueOf(mat.group(0));
//							this.skillMap.put(entry.getKey().trim().toLowerCase(), level/5.0f);
//						}
//					}
//			this.realSkillMap=(HashMap<String, Float>) this.skillMap.clone();
//			this.maxAMap=(HashMap<String, Float>) this.skillMap.clone();
//			}catch(Exception e){
//				System.out.println(this.id);
//				e.printStackTrace();
//			}
//		}
		return skillMap;
	}
	
	public HashMap<String, Float> getRealSkillMap() {
		if(this.realSkillMap==null){
			this.getSkillMap();
		}
		return this.realSkillMap;
	}

	public void setRealSkillMap(HashMap<String, Float> realSkillMap) {
		this.realSkillMap = realSkillMap;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setSkillMap(HashMap<String, Float> skillMap) {
		this.skillMap = skillMap;
	}
	public float getSkillPoint(String skill){
		return this.getSkillMap().get(skill);
	}
	
	public HashMap<String, Float> getMaxAMap() {
		if(this.maxAMap==null){
			this.getSkillMap();
		}
		return maxAMap;
	}

	public void setMaxAMap(HashMap<String, Float> maxAMap) {
		this.maxAMap = maxAMap;
	}

//	@Override
//	public String toString() {
//		return "User [id=" + id + ", nickname=" + nickname + ", timeRangeInt=" + this.getTimeRangeInt()
//				+ ", javaSkill=" + this.getSkillMap().get("java") + ", skillMap=" + this.getSkillMap() + ", realSkillMap=" + this.getRealSkillMap()+", maxaMap=" + this.getMaxAMap()+"]";
//	}
//	@Override
//	public String toString() {
//		return "User [nickname=\t" + nickname + "\t, timeRangeInt=" + this.getTimeRangeInt()
//				+ ", java=" + this.getSkillMap().get("java") + ", skillMap=" + this.getSkillMap() + ", realSkillMap=" + this.getRealSkillMap()+", maxaMap=" + this.getMaxAMap()+"]";
//	}
//	@Override
//	public String toString() {
//		return "User [nickname=\t" + nickname + "\t, timeRangeInt=" + this.getTimeRangeInt()
//				+ ", java=" + this.getSkillMap().get("java") + "]--";
//	}
	@Override
	public String toString() {
		return  nickname;
	}
	public void updateSkill(String skill,float point){
		float rank=this.skillMap.get(skill);
		this.skillMap.put(skill, rank*0.8f+0.2f*point);
	}
	public void updateSkill2(String skill,float point){
		float rank=this.skillMap.get(skill);
		float maxa=this.maxAMap.get(skill);
		if(maxa<point){
			maxa=point;
			this.maxAMap.put(skill,maxa);
		}
		float add=(float)((maxa-rank)/(1f+Math.exp(5*(rank-point))));
//		System.out.println("add"+add);
//		System.out.println("rank"+rank);
		this.skillMap.put(skill, rank+add);
	}
	public float getFangcha(String skill){
		float rank=this.getSkillMap().get(skill);
		float realRank=this.getRealSkillMap().get(skill);
		float cha=realRank-rank;
		return cha*cha;
	}
	public void signUp(Task task){
		task.getRegister().add(this);
	}
}
